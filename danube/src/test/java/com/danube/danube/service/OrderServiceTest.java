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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        UUID expectedCustomerId = UUID.randomUUID();
        OrderInformationDTO expectedOrderInformation = new OrderInformationDTO(
                "Street Address",
                "Example City",
                "Example State",
                "Example Country",
                1234,
                expectedCustomerId
        );

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> orderService.saveOrderInformation(expectedOrderInformation));
    }

    @Test
    void testSaveOrderInformation_WithOrderRepositoryReturnsEmptyList_ShouldThrowOrderFailedException(){
        UUID expectedCustomerId = UUID.randomUUID();
        OrderInformationDTO expectedOrderInformation = new OrderInformationDTO(
                "Street Address",
                "Example City",
                "Example State",
                "Example Country",
                1234,
                expectedCustomerId
        );

        UserEntity expectedUser = getExpectedUser(expectedCustomerId);

        Product expectedProduct = new Product();

        Order order = getOrder(
                false,
                3,
                false,
                expectedUser,
                expectedProduct
        );


        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(expectedUser));

        when(orderRepositoryMock.findAllByCustomer(expectedUser))
                .thenReturn(List.of(order));

        Order savedOrder = getOrder(
                false,
                3,
                false,
                expectedUser,
                expectedProduct,
                expectedOrderInformation.streetAddress(),
                expectedOrderInformation.city(),
                expectedOrderInformation.state(),
                expectedOrderInformation.country(),
                expectedOrderInformation.zip()
        );

        when(orderRepositoryMock.saveAll(List.of(savedOrder)))
                .thenReturn(List.of());

        assertThrowsExactly(OrderFailedException.class, () -> orderService.saveOrderInformation(expectedOrderInformation));

    }

    @Test
    void testSetIsOrdered_WithNonExistingUser_ShouldThrowNonExistingUserException() {
        UUID expectedUserId = UUID.randomUUID();
        LocalDateTime expectedDateTime = LocalDateTime.now();
        when(userRepositoryMock.findById(expectedUserId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> orderService.setIsOrdered(expectedUserId, expectedDateTime));
    }

    @Test
    void testSetIsOrdered_WithExistingUser_ShouldCallOnceOrderRepositorySaveAll(){
        UUID expectedCustomerId = UUID.randomUUID();
        UserEntity expectedUser = getExpectedUser(expectedCustomerId);

        LocalDateTime expectedDateTime = LocalDateTime.now();

        Order firstOrder = getOrder(
                true,
                3,
                false,
                expectedUser,
                new Product()
        );

        Order secondOrder = getOrder(
                true,
                5,
                false,
                expectedUser,
                new Product()
        );

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(expectedUser));

        when(orderRepositoryMock.findAllByCustomer(expectedUser))
                .thenReturn(List.of(
                        firstOrder,
                        secondOrder
                ));

        orderService.setIsOrdered(expectedCustomerId, expectedDateTime);

        verify(orderRepositoryMock, times(1)).saveAll(List.of(
                firstOrder,
                secondOrder
        ));
    }

    private UserEntity getExpectedUser(UUID expectedCustomerId){
        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(expectedCustomerId);
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        return expectedUser;
    }

    private UserEntity getExpectedUser(
            UUID expectedCustomerId,
            String email,
            String firstName,
            String lastName
    ){
        UserEntity expectedUser = new UserEntity();
        expectedUser.setId(expectedCustomerId);
        expectedUser.setEmail(email);
        expectedUser.setFirstName(firstName);
        expectedUser.setLastName(lastName);

        return expectedUser;
    }

    private Order getOrder(
            boolean isOrdered,
            int quantity,
            boolean isSent,
            UserEntity expectedUser,
            Product expectedProduct
    ){
        Order order = new Order();
        order.setOrdered(isOrdered);
        order.setOrderTime(Timestamp.valueOf(LocalDateTime.now()));
        order.setQuantity(quantity);
        order.setSent(isSent);
        order.setCustomer(expectedUser);
        order.setProduct(expectedProduct);

        return order;
    }

    private  Order getOrder(
            boolean isOrdered,
            int quantity,
            boolean isSent,
            UserEntity expectedUser,
            Product expectedProduct,
            String streetAddress,
            String city,
            String state,
            String country,
            int zip
    ){
        Order savedOrder = new Order();
        savedOrder.setOrdered(isOrdered);
        savedOrder.setOrderTime(Timestamp.valueOf(LocalDateTime.now()));
        savedOrder.setQuantity(quantity);
        savedOrder.setSent(isSent);
        savedOrder.setCustomer(expectedUser);
        savedOrder.setProduct(expectedProduct);
        savedOrder.setStreetAddress(streetAddress);
        savedOrder.setCity(city);
        savedOrder.setState(state);
        savedOrder.setCountry(country);
        savedOrder.setZip(zip);

        return savedOrder;
    }
}