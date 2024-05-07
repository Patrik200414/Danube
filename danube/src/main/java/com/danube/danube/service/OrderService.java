package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.NotEnoughQuantityToOrderException;
import com.danube.danube.custom_exception.order.OrderNotFoundException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.CartItemResponseDTO;
import com.danube.danube.model.dto.order.CartItemShowDTO;
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

import java.util.List;
import java.util.Optional;

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

        Optional<Order> searchedOrderByCustomer = orderRepository.findByCustomerAndProduct(customer, product);

        product.setQuantity(remainedQuantity);
        productRepository.save(product);

        Order orderItem = validateIfOrderAlreadyExists(cartElement, searchedOrderByCustomer, customer, product);
        return converter.convertOrderToCarItemShowDTO(orderRepository.save(orderItem));
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

        orderRepository.delete(order);
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
