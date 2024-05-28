package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.OrderFailedException;
import com.danube.danube.model.dto.order.OrderInformationDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.order.OrderRepository;
import com.danube.danube.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public void saveOrderInformation(OrderInformationDTO orderInformation){
        UserEntity customer = userRepository.findById(orderInformation.customerId())
                .orElseThrow(NonExistingUserException::new);

        List<Order> ordersByCustomer = orderRepository.findAllByCustomer(customer);
        ordersByCustomer.stream()
                .forEach(order -> {
                    order.setCity(orderInformation.city());
                    order.setCountry(orderInformation.country());
                    order.setState(orderInformation.state());
                    order.setStreetAddress(orderInformation.streetAddress());
                    order.setZip(orderInformation.zip());
                });

        List<Order> savedOrderInformation = orderRepository.saveAll(ordersByCustomer);
        if(savedOrderInformation.size() != ordersByCustomer.size()){
            throw new OrderFailedException();
        }
    }
}
