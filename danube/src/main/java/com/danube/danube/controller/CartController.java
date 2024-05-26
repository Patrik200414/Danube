package com.danube.danube.controller;

import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.CartItemResponseDTO;
import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.model.dto.order.ItemIntegrationDTO;
import com.danube.danube.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final OrderService orderService;

    @Autowired
    public CartController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public CartItemShowDTO addToCart(@RequestBody AddToCartDTO cartItem){
        return orderService.addToCart(cartItem);
    }

    @PostMapping("/integrate")
    public CartItemResponseDTO integrateItemsToUser(@RequestBody ItemIntegrationDTO cartItems){
        List<CartItemShowDTO> cartItemShowDTOS = orderService.integrateCartItemsToUser(cartItems);
        return new CartItemResponseDTO(cartItemShowDTOS);
    }

    @GetMapping("/{customerId}")
    public CartItemResponseDTO getMyCart(@PathVariable long customerId){
        return new CartItemResponseDTO(orderService.getCartItems(customerId));
    }

    @DeleteMapping("/{orderId}")
    public HttpStatus deleteOrder(@PathVariable long orderId){
        orderService.deleteOrder(orderId);
        return HttpStatus.NO_CONTENT;
    }
}
