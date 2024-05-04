package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.NotEnoughQuantityToOrderException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.order.OrderRepository;
import com.danube.danube.repository.product.ProductRepository;
import com.danube.danube.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void addToCart(AddToCartDTO cartElement){
        UserEntity customer = userRepository.findById(cartElement.customerId())
                .orElseThrow(NonExistingUserException::new);

        Product product = productRepository.findById(cartElement.productId())
                .orElseThrow(NonExistingProductException::new);
        int remainedQuantity = product.getQuantity() - cartElement.quantity();
        if(remainedQuantity < 0){
            throw new NotEnoughQuantityToOrderException(cartElement.quantity(), product.getQuantity());
        }

        product.setQuantity(remainedQuantity);
        productRepository.save(product);
        Order orderItem = createNewOrder(customer, product, cartElement.quantity());
        orderRepository.save(orderItem);
    }

    private Order createNewOrder(UserEntity customer, Product product, int quantity) {
        Order orderItem = new Order();
        orderItem.setCustomer(customer);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        return orderItem;
    }
}
