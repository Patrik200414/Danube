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
        UserEntity expectedUser = getExpectedUserEntity(expectedCustomerId);

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
        UserEntity expectedUser = getExpectedUserEntity(expectedCustomerId);

        Product expectedProduct = new Product();
        expectedProduct.setQuantity(9);

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
        UserEntity expectedUser = getExpectedUserEntity(expectedCustomerId);

        Product expectedProduct = getProduct(20, 1);

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

        Order expectedOrder = getExpectedOrder(expectedProduct, addToCartDTO.quantity(), 0);
        expectedOrder.setCustomer(expectedUser);

        cartService.addToCart(addToCartDTO);

        expectedProduct.setQuantity(10);
        verify(productRepositoryMock, times(1)).save(expectedProduct);
        verify(orderRepositoryMock, times(1)).save(expectedOrder);
    }

    @Test
    void addToCart_WithExistingUserAndExistingProductAndEnoughQuantityAndFindByCustomerAndProductAndIsOrderedFalseReturns$_ShouldSaveOrder() throws DataFormatException, IOException {
        UUID expectedCustomerId = UUID.randomUUID();
        UserEntity expectedUser = getExpectedUserEntity(expectedCustomerId);
        int expectedProductQuantity = 20;
        int addToCartQuantity = 10;
        int expectedOrderQuantity = 5;
        long productId = 1;

        Product expectedProduct = new Product();
        expectedProduct.setQuantity(expectedProductQuantity);

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                expectedCustomerId,
                productId,
                addToCartQuantity
        );

        Order expectedOrder = getExpectedOrder(expectedUser, expectedProduct, expectedOrderQuantity);

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(
                        expectedUser
                ));

        when(productRepositoryMock.findById(productId))
                .thenReturn(Optional.of(expectedProduct));

        when(orderRepositoryMock.findByCustomerAndProductAndIsOrderedFalse(expectedUser, expectedProduct))
                .thenReturn(Optional.of(expectedOrder));

        cartService.addToCart(addToCartDTO);

        expectedProduct.setQuantity(10);
        expectedOrder.setQuantity(expectedOrderQuantity + addToCartQuantity);
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
        List<CartItemShowDTO> expectedIntegrityItems = getExpectedIntegrityItem();

        ItemIntegrationDTO expectedIntegration = new ItemIntegrationDTO(
                expectedCustomerId,
                expectedIntegrityItems
        );

        UserEntity expectedUser = getExpectedUserEntity(expectedCustomerId);

        Product firstProduct = getProduct(30, 1);
        Product secondProduct = getProduct(40, 2);

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(expectedUser));

        when(productRepositoryMock.findAllById(List.of(
                expectedIntegrityItems.getFirst().id(),
                expectedIntegrityItems.getLast().id()))
        )
                .thenReturn(List.of(
                        firstProduct,
                        secondProduct
                ));

        Order firstExpectedOrder = getExpectedOrder(firstProduct, 3, 1);
        Order secondExpectedOrder = getExpectedOrder(secondProduct, 5, 2);

        when(orderRepositoryMock.findAllByProductIsInAndCustomerAndNotOrdered(List.of(firstProduct, secondProduct), expectedUser))
                .thenReturn(List.of(firstExpectedOrder, secondExpectedOrder));


        cartService.integrateCartItemsToUser(expectedIntegration);

        setProductQuantity(
                firstProduct,
                secondProduct,
                expectedIntegrityItems.getFirst(),
                expectedIntegrityItems.getLast()
        );

        setOrderQuantity(
                firstExpectedOrder,
                secondExpectedOrder,
                expectedIntegration,
                expectedIntegration
        );

        verify(productRepositoryMock, times(1)).saveAll(List.of(firstProduct, secondProduct));
        verify(orderRepositoryMock, times(1)).saveAll(List.of(firstExpectedOrder, secondExpectedOrder));
    }

    


    @Test
    void testIntegrateCartItemToUser_WithExistingUserAndExistingProductAndUserNotHaveItemsInThereCart_ShouldExecuteSaveAllOnProductAndOrderrepositoryWithExpectedValues() throws DataFormatException, IOException {
        UUID expectedCustomerId = UUID.randomUUID();

        List<CartItemShowDTO> expectedIntegrityItems = getExpectedIntegrityItem();

        ItemIntegrationDTO expectedIntegration = new ItemIntegrationDTO(
                expectedCustomerId,
                expectedIntegrityItems
        );

        UserEntity expectedUser = getExpectedUserEntity(expectedCustomerId);

        Product firstProduct = getProduct(30, 1);
        Product secondProduct = getProduct(40, 2);

        when(userRepositoryMock.findById(expectedCustomerId))
                .thenReturn(Optional.of(expectedUser));

        when(productRepositoryMock.findAllById(List.of(
                        expectedIntegrityItems.getFirst().id(),
                        expectedIntegrityItems.getLast().id())
                )
        )
                .thenReturn(List.of(
                        firstProduct,
                        secondProduct
                ));

        Order firstExpectedOrder = getExpectedOrder(firstProduct, 3, 0);

        Order secondExpectedOrder = getExpectedOrder(secondProduct, 5, 0);

        when(orderRepositoryMock.findAllByProductIsInAndCustomerAndNotOrdered(List.of(firstProduct, secondProduct), expectedUser))
                .thenReturn(List.of());


        cartService.integrateCartItemsToUser(expectedIntegration);

        setProductQuantity(
                firstProduct,
                secondProduct,
                expectedIntegrityItems.getFirst(),
                expectedIntegrityItems.getLast()
        );

        setOrderQuantity(
                firstExpectedOrder,
                secondExpectedOrder,
                expectedIntegration,
                expectedIntegration
        );

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
        UUID expectedCustomerId = UUID.randomUUID();
        UserEntity expectedUser = getExpectedUserEntity(expectedCustomerId);

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
        Product expectedProduct = getProduct(5, 1);
        Order expectedOrder = getExpectedOrder(expectedProduct, 3, 1);

        when(orderRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expectedOrder));

        cartService.deleteOrder(1);
        expectedProduct.setQuantity(expectedProduct.getQuantity() + expectedOrder.getQuantity());

        verify(productRepositoryMock, times(1)).save(expectedProduct);
        verify(orderRepositoryMock, times(1)).delete(expectedOrder);
    }

    private UserEntity getExpectedUserEntity(UUID expectedCustomerId) {
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setId(expectedCustomerId);
        expectedUser.setFirstName("Test");
        expectedUser.setLastName("User");
        return expectedUser;
    }

    private Order getExpectedOrder(UserEntity expectedUser, Product expectedProduct, int expectedQuantity) {
        Order expectedOrder = new Order();
        expectedOrder.setCustomer(expectedUser);
        expectedOrder.setProduct(expectedProduct);
        expectedOrder.setQuantity(expectedQuantity);
        return expectedOrder;
    }

    private Order getExpectedOrder(Product product, int quantity, long id) {
        Order firstExpectedOrder = new Order();
        firstExpectedOrder.setQuantity(quantity);
        firstExpectedOrder.setId(id);
        firstExpectedOrder.setProduct(product);
        return firstExpectedOrder;
    }

    private Product getProduct(int quantity, long id) {
        Product firstProduct = new Product();
        firstProduct.setQuantity(quantity);
        firstProduct.setId(id);
        return firstProduct;
    }

    private void setOrderQuantityAfterModification(Order expectedOrder, ItemIntegrationDTO expectedIntegration) {
        int orderExpectedQuantityAfterProductOrderAdded = expectedOrder.getQuantity() + expectedIntegration.products().getFirst().orderedQuantity();
        expectedOrder.setQuantity(orderExpectedQuantityAfterProductOrderAdded);
    }

    private static void setProductQuantityAfterModification(Product product, CartItemShowDTO ExpectedIntegrityItem) {
        int productExpectedQuantityAfterOrderSubtraction = product.getQuantity() - ExpectedIntegrityItem.orderedQuantity();
        product.setQuantity(productExpectedQuantityAfterOrderSubtraction);
    }

    private List<CartItemShowDTO> getExpectedIntegrityItem(){
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

        return List.of(
                firstExpectedIntegrityItem,
                secondExpectedIntegrityItem
        );
    }

    private void setProductQuantity(
            Product firstProduct,
            Product secondProduct,
            CartItemShowDTO firstIntegrityItem,
            CartItemShowDTO secondIntegrityItem
    ){
        setProductQuantityAfterModification(firstProduct, firstIntegrityItem);
        setProductQuantityAfterModification(secondProduct, secondIntegrityItem);
    }

    private void setOrderQuantity(
            Order firstorder,
            Order secondOrder,
            ItemIntegrationDTO firstIntegration,
            ItemIntegrationDTO secondIntegration
    ){
        setOrderQuantityAfterModification(firstorder, firstIntegration);
        setOrderQuantityAfterModification(secondOrder, secondIntegration);
    }

}