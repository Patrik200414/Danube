package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.NotEnoughQuantityToOrderException;
import com.danube.danube.custom_exception.order.OrderNotFoundException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.model.dto.order.*;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.order.OrderRepository;
import com.danube.danube.repository.product.ProductRepository;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.utility.converter.Converter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final Converter converter;

    @Autowired
    public OrderService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository, Converter converter) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.converter = converter;
    }

    @Transactional
    public CartItemShowDTO addToCart(AddToCartDTO cartElement){
        UserEntity customer = userRepository.findById(cartElement.customerId())
                .orElseThrow(NonExistingUserException::new);

        Product product = productRepository.findById(cartElement.productId())
                .orElseThrow(NonExistingProductException::new);
        int remainedQuantity = product.getQuantity() - cartElement.quantity();
        if(remainedQuantity < 0){
            throw new NotEnoughQuantityToOrderException(cartElement.quantity(), product.getQuantity());
        }

        Order orderItem = modifyProductAndOrderQuantity(cartElement, customer, product, remainedQuantity);
        return converter.convertOrderToCarItemShowDTO(orderRepository.save(orderItem));
    }

    @Transactional
    public List<CartItemShowDTO> integrateCartItemsToUser(ItemIntegrationDTO cartItems){
        UserEntity customer = userRepository.findById(cartItems.customerId())
                .orElseThrow(NonExistingUserException::new);

        Map<Long, Order> ordersByCustomer = getExistingOrdersByCustomer(cartItems, customer);


        List<Order> updatedOrdersThatIsInUserCart = new ArrayList<>();
        List<Product> updatedProductsThatIsInUserCart = new ArrayList<>();


        handleOrderCreation(cartItems, ordersByCustomer, updatedOrdersThatIsInUserCart, updatedProductsThatIsInUserCart, customer);

        productRepository.saveAll(updatedProductsThatIsInUserCart);
        orderRepository.saveAll(updatedOrdersThatIsInUserCart);

        List<Order> allByCustomer = orderRepository.findAllByCustomer(customer);

        return allByCustomer.stream()
                .map(cartItem -> converter.convertOrderToCarItemShowDTO(cartItem))
                .toList();
    }

    public List<CartItemShowDTO> getCartItems(long customerId){
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(NonExistingUserException::new);

        List<Order> cartItems = orderRepository.findAllByCustomer(customer);

        return cartItems.stream()
                .map(cartItem -> converter.convertOrderToCarItemShowDTO(cartItem))
                .toList();
    }

    public void deleteOrder(long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        Product product = order.getProduct();
        product.setQuantity(product.getQuantity() + order.getQuantity());

        productRepository.save(product);
        orderRepository.delete(order);
    }

    private void handleOrderCreation(ItemIntegrationDTO cartItems, Map<Long, Order> ordersByCustomer, List<Order> updatedOrdersThatIsInUserCart, List<Product> updatedProductsThatIsInUserCart, UserEntity customer) {
        Map<Long, Integer> newlyOrderedProductIdsAndOrderedQuantities = new HashMap<>();
        handleAlreadyExistingOrderCreation(cartItems, ordersByCustomer, updatedOrdersThatIsInUserCart, updatedProductsThatIsInUserCart, newlyOrderedProductIdsAndOrderedQuantities);


        List<Long> newProductsIds = newlyOrderedProductIdsAndOrderedQuantities.keySet().stream()
                .toList();

        List<Product> newProducts = productRepository.findAllById(newProductsIds);
        handleNewOrderCreation(updatedOrdersThatIsInUserCart, updatedProductsThatIsInUserCart, customer, newProducts, newlyOrderedProductIdsAndOrderedQuantities);
    }

    private void handleNewOrderCreation(List<Order> updatedOrdersThatIsInUserCart, List<Product> updatedProductsThatIsInUserCart, UserEntity customer, List<Product> newProducts, Map<Long, Integer> newlyOrderedProductIdsAndOrderedQuantities) {
        for(Product product : newProducts){
            int remainder = product.getQuantity() - newlyOrderedProductIdsAndOrderedQuantities.get(product.getId());
            if(remainder > 0){
                product.setQuantity(remainder);
                Order newOrder = createNewOrder(customer, product, newlyOrderedProductIdsAndOrderedQuantities.get(product.getId()));

                updatedOrdersThatIsInUserCart.add(newOrder);
                updatedProductsThatIsInUserCart.add(product);
            }
        }
    }

    private void handleAlreadyExistingOrderCreation(ItemIntegrationDTO cartItems, Map<Long, Order> ordersByCustomer, List<Order> updatedOrdersThatIsInUserCart, List<Product> updatedProductsThatIsInUserCart, Map<Long, Integer> newlyOrderedProductIdsAndOrderedQuantities) {
        for(CartItemShowDTO cartItem : cartItems.products()){
            if(ordersByCustomer.containsKey(cartItem.id())){
                Order alreadyStoredOrder = ordersByCustomer.get(cartItem.id());
                Product alreadyOrderedProduct = alreadyStoredOrder.getProduct();

                int remainder = alreadyOrderedProduct.getQuantity() - cartItem.orderedQuantity();
                if(remainder > 0){
                    handleQuantityChange(updatedOrdersThatIsInUserCart, updatedProductsThatIsInUserCart, cartItem, alreadyStoredOrder, alreadyOrderedProduct);
                }
            } else {
                newlyOrderedProductIdsAndOrderedQuantities.put(cartItem.id(), cartItem.orderedQuantity());
            }
        }
    }

    private void handleQuantityChange(List<Order> updatedOrdersThatIsInUserCart, List<Product> updatedProductsThatIsInUserCart, CartItemShowDTO cartItem, Order alreadyStoredOrder, Product alreadyOrderedProduct) {
        alreadyStoredOrder.setQuantity(alreadyStoredOrder.getQuantity() + cartItem.orderedQuantity());
        alreadyOrderedProduct.setQuantity(alreadyOrderedProduct.getQuantity() - cartItem.orderedQuantity());

        updatedOrdersThatIsInUserCart.add(alreadyStoredOrder);
        updatedProductsThatIsInUserCart.add(alreadyOrderedProduct);
    }

    private Map<Long, Order> getExistingOrdersByCustomer(ItemIntegrationDTO cartItems, UserEntity customer) {
        //Create map of orders by customer
        List<Long> productIds = cartItems.products().stream()
                .map(CartItemShowDTO::id)
                .toList();
        List<Product> productsById = productRepository.findAllById(productIds);

        List<Order> customerOrders = orderRepository.findAllByProductIsInAndCustomer(productsById, customer);
        return customerOrders.stream()
                .collect(Collectors.toMap(Order::getId, order -> order));
    }

    private Order modifyProductAndOrderQuantity(AddToCartDTO cartElement, UserEntity customer, Product product, int remainedQuantity) {
        Optional<Order> searchedOrderByCustomer = orderRepository.findByCustomerAndProduct(customer, product);

        product.setQuantity(remainedQuantity);
        productRepository.save(product);

        return validateIfOrderAlreadyExists(cartElement, searchedOrderByCustomer, customer, product);
    }

    private Order validateIfOrderAlreadyExists(AddToCartDTO cartElement, Optional<Order> searchedOrderByCustomer, UserEntity customer, Product product) {
        Order orderItem;
        if(searchedOrderByCustomer.isEmpty()){
            orderItem = createNewOrder(customer, product, cartElement.quantity());
        } else {
            orderItem = searchedOrderByCustomer.get();
            orderItem.setQuantity(orderItem.getQuantity() + cartElement.quantity());
        }
        return orderItem;
    }

    private Order createNewOrder(UserEntity customer, Product product, int quantity) {
        Order orderItem = new Order();
        orderItem.setCustomer(customer);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        return orderItem;
    }
}
