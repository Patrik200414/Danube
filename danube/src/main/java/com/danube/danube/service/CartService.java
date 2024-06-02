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
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final Converter converter;

    @Autowired
    public CartService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository, Converter converter) {
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

        Optional<Order> searchedOrderByCustomer = modifyProductAndOrderQuantity(customer, product, remainedQuantity);
        Order orderItem = validateIfOrderAlreadyExists(cartElement.quantity(), searchedOrderByCustomer, customer, product);

        return converter.convertOrderToCarItemShowDTO(orderRepository.save(orderItem));
    }

    @Transactional
    public List<CartItemShowDTO> integrateCartItemsToUser(ItemIntegrationDTO cartItems){
        UserEntity customer = userRepository.findById(cartItems.customerId())
                .orElseThrow(NonExistingUserException::new);

        Map<Long, Order> ordersByCustomer = getExistingOrdersByCustomer(cartItems, customer);

        handleOrderCreation(cartItems, ordersByCustomer, customer);

        List<Order> allByCustomer = orderRepository.findAllByCustomerIsOrderedFalse(customer);

        return allByCustomer.stream()
                .map(cartItem -> converter.convertOrderToCarItemShowDTO(cartItem))
                .toList();
    }

    public List<CartItemShowDTO> getCartItems(long customerId){
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(NonExistingUserException::new);

        List<Order> cartItems = orderRepository.findAllByCustomer(customer);

        return cartItems.stream()
                .filter(cartItem -> !cartItem.isOrdered())
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

    private void handleOrderCreation(ItemIntegrationDTO cartItems, Map<Long, Order> ordersByCustomer, UserEntity customer) {
        /*
         * Checks if the user is already has the same item in the cart
         * If the item exists in the cart than increment the order quantity
         * Else it will create a new item in the cart
         */
        List<Order> updatedOrdersThatIsInUserCart = new ArrayList<>();
        List<Product> updatedProductsThatIsInUserCart = new ArrayList<>();

        Map<Long, Integer> newlyOrderedProductIdsAndOrderedQuantities = new HashMap<>();
        handleAlreadyExistingOrderCreation(cartItems, ordersByCustomer, updatedOrdersThatIsInUserCart, updatedProductsThatIsInUserCart, newlyOrderedProductIdsAndOrderedQuantities);


        List<Long> newProductsIds = newlyOrderedProductIdsAndOrderedQuantities.keySet().stream()
                .toList();

        List<Product> newProducts = productRepository.findAllById(newProductsIds);
        handleNewOrderCreation(updatedOrdersThatIsInUserCart, updatedProductsThatIsInUserCart, customer, newProducts, newlyOrderedProductIdsAndOrderedQuantities);

        productRepository.saveAll(updatedProductsThatIsInUserCart);
        orderRepository.saveAll(updatedOrdersThatIsInUserCart);
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
        /*
         * Modifies the order's quantity and the product remained quantity if the product exists in the user's order
         * If doesn't exist than creates an entry to the newly ordered map
         */
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
        /*
         * Change the ordered item quantity
         * Change the ordered product remained quantity
         * */
        alreadyStoredOrder.setQuantity(alreadyStoredOrder.getQuantity() + cartItem.orderedQuantity());
        alreadyOrderedProduct.setQuantity(alreadyOrderedProduct.getQuantity() - cartItem.orderedQuantity());

        /*
        * Add the modified order to the user's order list
        * Add the modified product to the product list
        * */
        updatedOrdersThatIsInUserCart.add(alreadyStoredOrder);
        updatedProductsThatIsInUserCart.add(alreadyOrderedProduct);
    }

    private Map<Long, Order> getExistingOrdersByCustomer(ItemIntegrationDTO cartItems, UserEntity customer) {
        //Create map of orders by customer
        List<Long> productIds = cartItems.products().stream()
                .map(CartItemShowDTO::id)
                .toList();
        List<Product> productsByIds = productRepository.findAllById(productIds);

        List<Order> customerOrders = orderRepository.findAllByProductIsInAndCustomerAndNotOrdered(productsByIds, customer);
        return customerOrders.stream()
                .collect(Collectors.toMap(order -> order.getProduct().getId(), order -> order));
    }

    private Optional<Order> modifyProductAndOrderQuantity(UserEntity customer, Product product, int remainedQuantity) {
        Optional<Order> searchedOrderByCustomer = orderRepository.findByCustomerAndProductAndIsOrderedFalse(customer, product);

        product.setQuantity(remainedQuantity);
        productRepository.save(product);

        return searchedOrderByCustomer;
    }

    private Order validateIfOrderAlreadyExists(int orderedQuantity, Optional<Order> searchedOrderByCustomer, UserEntity customer, Product product) {
        Order orderItem;
        if(searchedOrderByCustomer.isEmpty()){
            orderItem = createNewOrder(customer, product, orderedQuantity);
        } else {
            orderItem = searchedOrderByCustomer.get();
            orderItem.setQuantity(orderItem.getQuantity() + orderedQuantity);
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
