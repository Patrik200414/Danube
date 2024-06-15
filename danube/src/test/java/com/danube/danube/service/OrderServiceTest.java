package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.OrderFailedException;
import com.danube.danube.model.dto.order.OrderInformationDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.order.OrderRepository;
import com.danube.danube.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    OrderService orderService;
    OrderRepository orderRepositoryMock;
    UserRepository userRepositoryMock;

    @BeforeEach
    void setup(){
        orderRepositoryMock = mock(OrderRepository.class);
        userRepositoryMock = mock(UserRepository.class);

        orderService = new OrderService(orderRepositoryMock, userRepositoryMock);
    }

    @Test
    void testSaveOrderInformation_WithNonExistingUser_ShouldThrowNonExistingUserException() {
        OrderInformationDTO expectedOrderInformation = new OrderInformationDTO(
                "Street Address",
                "Example City",
                "Example State",
                "Example Country",
                1234,
                1
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> orderService.saveOrderInformation(expectedOrderInformation));
    }

    @Test
    void testSaveOrderInformation_WithOrderRepositoryReturnsEmptyList_ShouldThrowOrderFailedException(){
        OrderInformationDTO expectedOrderInformation = new OrderInformationDTO(
                "Street Address",
                "Example City",
                "Example State",
                "Example Country",
                1234,
                1
        );

        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(1);
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        Product expectedProduct = new Product();

        Order order = new Order();
        order.setOrdered(false);
        order.setOrderTime(Timestamp.valueOf(LocalDateTime.now()));
        order.setQuantity(3);
        order.setSent(false);
        order.setCustomer(expectedUser);
        order.setProduct(expectedProduct);


        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedUser));

        when(orderRepositoryMock.findAllByCustomer(expectedUser))
                .thenReturn(List.of(order));

        Order savedOrder = new Order();
        savedOrder.setOrdered(false);
        savedOrder.setOrderTime(Timestamp.valueOf(LocalDateTime.now()));
        savedOrder.setQuantity(3);
        savedOrder.setSent(false);
        savedOrder.setCustomer(expectedUser);
        savedOrder.setProduct(expectedProduct);
        savedOrder.setStreetAddress(expectedOrderInformation.streetAddress());
        savedOrder.setCity(expectedOrderInformation.city());
        savedOrder.setState(expectedOrderInformation.state());
        savedOrder.setCountry(expectedOrderInformation.country());
        savedOrder.setZip(expectedOrderInformation.zip());

        when(orderRepositoryMock.saveAll(List.of(savedOrder)))
                .thenReturn(List.of());

        assertThrowsExactly(OrderFailedException.class, () -> orderService.saveOrderInformation(expectedOrderInformation));

    }

    @Test
    void setIsOrdered() {
        /*
        * TODO: finish testing this method
        *  */
    }
}