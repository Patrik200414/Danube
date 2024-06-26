package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.order.NotEnoughQuantityToOrderException;
import com.danube.danube.custom_exception.order.OrderNotFoundException;
import com.danube.danube.custom_exception.product.NonExistingProductException;
import com.danube.danube.model.dto.image.ImageShow;
import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.order.ItemIntegrationDTO;
import com.danube.danube.model.order.Order;
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
import java.util.UUID;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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


        cartService = new CartService(userRepositoryMock, productRepositoryMock, orderRepositoryMock, productViewConverterMock, imageUtilityMock);
    }

    @Test
    void testAddToCart_WithNonExistingUser_ShouldThrowNonExistingUserException(){
        UUID expectedCustomerId = UUID.randomUUID();
        AddToCartDTO addToCartDTO = new AddToCartDTO(
                expectedCustomerId,
                1,
                10
        );

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> cartService.addToCart(addToCartDTO));
    }

    @Test
    void testAddToCart_WithNotExistingProduct_ShouldThrowNonExistingProductException(){
        UUID expectedCustomerId = UUID.randomUUID();
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                expectedCustomerId,
                1,
                10
        );

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(
                        expectedUser
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingProductException.class, () -> cartService.addToCart(addToCartDTO));
    }

    @Test
    void testAddToCart_WithXQuantityInAddToCartDTOIsLargerThanProductQuantity_ShouldThrowNotEnoughQuantityToOrderException(){
        UUID expectedCustomerId = UUID.randomUUID();
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        Product expectedProduct = new Product();
        expectedProduct.setQuantity(9);;

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                expectedCustomerId,
                1,
                10
        );

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(
                        expectedUser
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedProduct));


        assertThrowsExactly(NotEnoughQuantityToOrderException.class, () -> cartService.addToCart(addToCartDTO));
    }

    @Test
    void addToCart_WithExistingUserAndExistingProductAndEnoughQuantityAndFindByCustomerAndProductAndIsOrderedFalseReturnsEmptyOptional_ShouldSaveOrder() throws DataFormatException, IOException {
        UUID expectedCustomerId = UUID.randomUUID();
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        Product expectedProduct = new Product();
        expectedProduct.setQuantity(20);;

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                expectedCustomerId,
                1,
                10
        );

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(
                        expectedUser
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedProduct));

        when(orderRepositoryMock.findByCustomerAndProductAndIsOrderedFalse(expectedUser, expectedProduct))
                .thenReturn(Optional.empty());

        Order expectedOrder = new Order();
        expectedOrder.setCustomer(expectedUser);
        expectedOrder.setProduct(expectedProduct);
        expectedOrder.setQuantity(addToCartDTO.quantity());

        cartService.addToCart(addToCartDTO);


        expectedProduct.setQuantity(10);
        verify(productRepositoryMock, times(1)).save(expectedProduct);
        verify(orderRepositoryMock, times(1)).save(expectedOrder);
    }

    @Test
    void addToCart_WithExistingUserAndExistingProductAndEnoughQuantityAndFindByCustomerAndProductAndIsOrderedFalseReturns$_ShouldSaveOrder() throws DataFormatException, IOException {
        UUID expectedCustomerId = UUID.randomUUID();
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        Product expectedProduct = new Product();
        expectedProduct.setQuantity(20);;

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                expectedCustomerId,
                1,
                10
        );

        Order expectedOrder = new Order();
        expectedOrder.setCustomer(expectedUser);
        expectedOrder.setProduct(expectedProduct);
        expectedOrder.setQuantity(5);

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(
                        expectedUser
                ));

        when(productRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedProduct));

        when(orderRepositoryMock.findByCustomerAndProductAndIsOrderedFalse(expectedUser, expectedProduct))
                .thenReturn(Optional.of(expectedOrder));



        cartService.addToCart(addToCartDTO);


        expectedProduct.setQuantity(10);
        expectedOrder.setQuantity(5 + addToCartDTO.quantity());
        verify(productRepositoryMock, times(1)).save(expectedProduct);
        verify(orderRepositoryMock, times(1)).save(expectedOrder);

    }


    @Test
    void testIntegrateCartItemsToUser_WithNonExistingUser_ShouldThrowNonExistingUserException() throws DataFormatException, IOException {
        UUID expectedCustomerId = UUID.randomUUID();
        ItemIntegrationDTO expectedIntegration = new ItemIntegrationDTO(
                expectedCustomerId,
                List.of()
        );

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.empty());

        assertThrowsExactly(NonExistingUserException.class, () -> cartService.integrateCartItemsToUser(expectedIntegration));
    }

    @Test
    void testIntegrateCartItemToUser_WithExistingUserAndExistingProductAndUserAlreadyHasItemsInThereCart_ShouldExecuteSaveAllOnProductAndOrderrepositoryWithExpectedValues() throws DataFormatException, IOException {
        UUID expectedCustomerId = UUID.randomUUID();
        CartItemShowDTO firstExpectedIntegrityItem = new CartItemShowDTO(
                1,
                "Product 1",
                50,
                new ImageShow("testImage1.jpg", new byte[1]),
                5,
                4.5
        );

        CartItemShowDTO secondExpectedIntegrityItem = new CartItemShowDTO(
                2,
                "Product 2",
                70,
                new ImageShow("testImage2.jpg", new byte[1]),
                3,
                4.0
        );

        ItemIntegrationDTO expectedIntegration = new ItemIntegrationDTO(
                expectedCustomerId,
                List.of(
                        firstExpectedIntegrityItem,
                        secondExpectedIntegrityItem
                )
        );

        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        Product firstProduct = new Product();
        firstProduct.setQuantity(30);
        firstProduct.setId(1);

        Product secondProduct = new Product();
        secondProduct.setQuantity(40);
        secondProduct.setId(2);

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(expectedUser));

        when(productRepositoryMock.findAllById(List.of(firstExpectedIntegrityItem.id(), secondExpectedIntegrityItem.id())))
                .thenReturn(List.of(
                        firstProduct,
                        secondProduct
                ));

        Order firstExpectedOrder = new Order();
        firstExpectedOrder.setQuantity(3);
        firstExpectedOrder.setId(1);
        firstExpectedOrder.setProduct(firstProduct);

        Order secondExpectedOrder = new Order();
        secondExpectedOrder.setQuantity(5);
        secondExpectedOrder.setId(2);
        secondExpectedOrder.setProduct(secondProduct);

        when(orderRepositoryMock.findAllByProductIsInAndCustomerAndNotOrdered(List.of(firstProduct, secondProduct), expectedUser))
                .thenReturn(List.of(firstExpectedOrder, secondExpectedOrder));


        cartService.integrateCartItemsToUser(expectedIntegration);

        firstProduct.setQuantity(firstProduct.getQuantity() - firstExpectedIntegrityItem.orderedQuantity());
        secondProduct.setQuantity(secondProduct.getQuantity() - secondExpectedIntegrityItem.orderedQuantity());

        firstExpectedOrder.setQuantity(firstExpectedOrder.getQuantity() + expectedIntegration.products().getFirst().orderedQuantity());
        secondProduct.setQuantity(secondProduct.getQuantity() + expectedIntegration.products().get(1).orderedQuantity());

        verify(productRepositoryMock, times(1)).saveAll(List.of(firstProduct, secondProduct));
        verify(orderRepositoryMock, times(1)).saveAll(List.of(firstExpectedOrder, secondExpectedOrder));
    }

    @Test
    void testIntegrateCartItemToUser_WithExistingUserAndExistingProductAndUserNotHaveItemsInThereCart_ShouldExecuteSaveAllOnProductAndOrderrepositoryWithExpectedValues() throws DataFormatException, IOException {
        UUID expectedCustomerId = UUID.randomUUID();

        CartItemShowDTO firstExpectedIntegrityItem = new CartItemShowDTO(
                1,
                "Product 1",
                50,
                new ImageShow("testImage1.jpg", new byte[1]),
                5,
                4.5
        );

        CartItemShowDTO secondExpectedIntegrityItem = new CartItemShowDTO(
                2,
                "Product 2",
                70,
                new ImageShow("testImage2.jpg", new byte[1]),
                3,
                4.0
        );

        ItemIntegrationDTO expectedIntegration = new ItemIntegrationDTO(
                expectedCustomerId,
                List.of(
                        firstExpectedIntegrityItem,
                        secondExpectedIntegrityItem
                )
        );

        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        Product firstProduct = new Product();
        firstProduct.setQuantity(30);
        firstProduct.setId(1);

        Product secondProduct = new Product();
        secondProduct.setQuantity(40);
        secondProduct.setId(2);

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(expectedUser));

        when(productRepositoryMock.findAllById(List.of(firstExpectedIntegrityItem.id(), secondExpectedIntegrityItem.id())))
                .thenReturn(List.of(
                        firstProduct,
                        secondProduct
                ));

        Order firstExpectedOrder = new Order();
        firstExpectedOrder.setQuantity(3);
        firstExpectedOrder.setProduct(firstProduct);

        Order secondExpectedOrder = new Order();
        secondExpectedOrder.setQuantity(5);
        secondExpectedOrder.setProduct(secondProduct);

        when(orderRepositoryMock.findAllByProductIsInAndCustomerAndNotOrdered(List.of(firstProduct, secondProduct), expectedUser))
                .thenReturn(List.of());


        cartService.integrateCartItemsToUser(expectedIntegration);

        firstProduct.setQuantity(firstProduct.getQuantity() - firstExpectedIntegrityItem.orderedQuantity());
        secondProduct.setQuantity(secondProduct.getQuantity() - secondExpectedIntegrityItem.orderedQuantity());

        firstExpectedOrder.setQuantity(firstExpectedOrder.getQuantity() + expectedIntegration.products().getFirst().orderedQuantity());
        secondExpectedOrder.setQuantity(secondExpectedOrder.getQuantity() + expectedIntegration.products().get(1).orderedQuantity());

        verify(productRepositoryMock, times(1)).saveAll(List.of(firstProduct, secondProduct));
        verify(orderRepositoryMock, times(1)).saveAll(List.of(firstExpectedOrder, secondExpectedOrder));
    }

    @Test
    void testGetCartItems_WithNonExistingUser_ShouldThrowNonExistingUserException() {
        UUID expectedCustomerId = UUID.randomUUID();

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.empty());


        assertThrowsExactly(NonExistingUserException.class, () -> cartService.getCartItems(expectedCustomerId));
    }

    @Test
    void testGetCartItems_WithExistingUser_ShouldExecuteOrderRepositoryFindAllByCustomerIsOrderedFalse() throws DataFormatException, IOException {
        UserEntity expectedUser = new UserEntity();

        UUID expectedCustomerId = UUID.randomUUID();

        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(expectedUser));

        cartService.getCartItems(expectedCustomerId);
        verify(orderRepositoryMock, times(1)).findAllByCustomerIsOrderedFalse(expectedUser);
    }

    @Test
    void testDeleteOrder_WithNonExistingOrder_ShouldThrowOrderNotFoundException() {
        when(orderRepositoryMock.findById(1L))
                .thenReturn(Optional.empty());

        assertThrowsExactly(OrderNotFoundException.class, () -> cartService.deleteOrder(1));
    }

    @Test
    void testDeleteOrder_WithExistingOrder_Should(){
        Product expectedProduct = new Product();
        expectedProduct.setQuantity(5);

        Order expectedOrder = new Order();
        expectedOrder.setQuantity(3);
        expectedOrder.setProduct(expectedProduct);

        when(orderRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedOrder));

        cartService.deleteOrder(1);
        expectedProduct.setQuantity(expectedProduct.getQuantity() + expectedOrder.getQuantity());

        verify(productRepositoryMock, times(1)).save(expectedProduct);
        verify(orderRepositoryMock, times(1)).delete(expectedOrder);
    }
}