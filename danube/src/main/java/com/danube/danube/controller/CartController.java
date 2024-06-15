package com.danube.danube.controller;

import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.CartItemResponseDTO;
import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.order.ItemIntegrationDTO;
import com.danube.danube.model.order.Order;
import com.danube.danube.service.CartService;
import com.danube.danube.utility.converter.productview.ProductViewConverter;
import com.danube.danube.utility.imageutility.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final ProductViewConverter productViewConverter;
    private final ImageUtility imageUtility;

    @Autowired
    public CartController(CartService cartService, ProductViewConverter productViewConverter, ImageUtility imageUtility) {
        this.cartService = cartService;
        this.productViewConverter = productViewConverter;
        this.imageUtility = imageUtility;
    }

    @PostMapping("")
    public CartItemShowDTO addToCart(@RequestBody AddToCartDTO cartItem) throws DataFormatException, IOException {
        Order order = cartService.addToCart(cartItem);
        return  productViewConverter.convertOrderToCarItemShowDTO(order, imageUtility);
    }

    @PostMapping("/integrate")
    public CartItemResponseDTO integrateItemsToUser(@RequestBody ItemIntegrationDTO cartItems) throws DataFormatException, IOException {
        List<Order> orders = cartService.integrateCartItemsToUser(cartItems);
        return new CartItemResponseDTO(productViewConverter.collectCartItemShowDTOs(orders, imageUtility));
    }

    @GetMapping("/{customerId}")
    public CartItemResponseDTO getMyCart(@PathVariable long customerId) throws DataFormatException, IOException {
        List<Order> cartItems = cartService.getCartItems(customerId);
        return new CartItemResponseDTO(productViewConverter.collectCartItemShowDTOs(cartItems, imageUtility));
    }

    @DeleteMapping("/{orderId}")
    public HttpStatus deleteOrder(@PathVariable long orderId){
        cartService.deleteOrder(orderId);
        return HttpStatus.NO_CONTENT;
    }
}
