package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.NotEnoughQuantityToOrderException;
import com.danube.danube.custom_exception.order.OrderNotFoundException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.ItemIntegrationDTO;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.order.OrderRepository;
import com.danube.danube.repository.product.ProductRepository;
import com.danube.danube.repository.user.UserRepository;
import com.danube.danube.utility.converter.productview.ProductViewConverter;
import com.danube.danube.utility.imageutility.ImageUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {
    CartService cartService;
    UserRepository userRepositoryMock;
    ProductRepository productRepositoryMock;
    OrderRepository orderRepositoryMock;
    ProductViewConverter productViewConverterMock;
    ImageUtility imageUtilityMock;

    @BeforeEach
    void setup(){
        userRepositoryMock = mock(UserRepository.class);
        productRepositoryMock = mock(ProductRepository.class);
        orderRepositoryMock = mock(OrderRepository.class);
        productViewConverterMock = mock(ProductViewConverter.class);
        imageUtilityMock = mock(ImageUtility.class);


        cartService = new CartService(userRepositoryMock, productRepositoryMock, orderRepositoryMock);
    }

    @Test
    void testAddToCart_WithNonExistingUser_ShouldThrowNonExistingUserException(){
        AddToCartDTO addToCartDTO = new AddToCartDTO(
                1,
                1,
                10
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> cartService.addToCart(addToCartDTO));
    }

    @Test
    void testAddToCart_WithNotExistingProduct_ShouldThrowNonExistingProductException(){
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(1);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                1,
                1,
                10
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(
                        expectedUser
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingProductException.class, () -> cartService.addToCart(addToCartDTO));
    }

    @Test
    void testAddToCart_WithXQuantityInAddToCartDTOIsLargerThanProductQuantity_ShouldThrowNotEnoughQuantityToOrderException(){
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(1);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        Product expectedProduct = new Product();
        expectedProduct.setQuantity(9);;

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                1,
                1,
                10
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.of(
                        expectedUser
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedProduct));


        assertThrowsExactly(NotEnoughQuantityToOrderException.class, () -> cartService.addToCart(addToCartDTO));
    }


    @Test
    void testIntegrateCartItemsToUser_WithNonExistingUser_ShouldThrowNonExistingUserException() throws DataFormatException, IOException {
        ItemIntegrationDTO expectedIntegration = new ItemIntegrationDTO(
                1,
                List.of()
        );

        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> cartService.integrateCartItemsToUser(expectedIntegration));
    }

    @Test
    void testIntegrateCartItemToUser_(){
        /*
        TODO: finish testing this method
         */
    }

    @Test
    void testGetCartItems_WithNonExistingUser_ShouldThrowNonExistingUserException() {
        when(userRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());


        assertThrowsExactly(NonExistingUserException.class, () -> cartService.getCartItems(1));
    }

    @Test
    void testDeleteOrder_WithNonExistingOrder_ShouldThrowOrderNotFoundException() {
        when(orderRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(OrderNotFoundException.class, () -> cartService.deleteOrder(1));
    }
}