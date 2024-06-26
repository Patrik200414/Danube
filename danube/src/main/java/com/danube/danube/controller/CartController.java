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
import java.util.UUID;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("")
    public CartItemShowDTO addToCart(@RequestBody AddToCartDTO cartItem) throws DataFormatException, IOException {
        return cartService.addToCart(cartItem);
    }

    @PostMapping("/integrate")
    public CartItemResponseDTO integrateItemsToUser(@RequestBody ItemIntegrationDTO cartItems) throws DataFormatException, IOException {
        return cartService.integrateCartItemsToUser(cartItems);
    }

    @GetMapping("/{customerId}")
    public CartItemResponseDTO getMyCart(@PathVariable UUID customerId) throws DataFormatException, IOException {
        return cartService.getCartItems(customerId);
    }

    @DeleteMapping("/{orderId}")
    public HttpStatus deleteOrder(@PathVariable long orderId){
        cartService.deleteOrder(orderId);
        return HttpStatus.NO_CONTENT;
    }
}
