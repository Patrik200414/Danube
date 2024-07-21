package com.danube.danube.controller;

import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.CartItemResponseDTO;
import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.order.ItemIntegrationDTO;
import com.danube.danube.service.CartService;
import com.danube.danube.utility.json.JsonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final JsonUtility jsonUtility;

    @Autowired
    public CartController(CartService cartService, JsonUtility jsonUtility) {
        this.cartService = cartService;
        this.jsonUtility = jsonUtility;
    }

    @PostMapping("")
    public CartItemShowDTO addToCart(@RequestBody String cartItem) throws DataFormatException, IOException {
        AddToCartDTO addToCartDTO =  jsonUtility.validateJson(cartItem, AddToCartDTO.class);
        return cartService.addToCart(addToCartDTO);
    }

    @PostMapping("/integrate")
    public CartItemResponseDTO integrateItemsToUser(@RequestBody String cartItems) throws DataFormatException, IOException {
        ItemIntegrationDTO itemIntegrationDTO = jsonUtility.validateJson(cartItems, ItemIntegrationDTO.class);
        return cartService.integrateCartItemsToUser(itemIntegrationDTO);
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
