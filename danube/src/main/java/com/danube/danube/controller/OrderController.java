package com.danube.danube.controller;

import com.danube.danube.model.dto.order.*;
import com.danube.danube.model.order.Order;
import com.danube.danube.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/cart")
    public CartItemShowDTO addToCart(@RequestBody AddToCartDTO cartItem){
        return orderService.addToCart(cartItem);
    }

    @PostMapping("/cart/integrate")
    public CartItemResponseDTO integrateItemsToUser(@RequestBody ItemIntegrationDTO cartItems){
        List<CartItemShowDTO> cartItemShowDTOS = orderService.integrateCartItemsToUser(cartItems);
        return new CartItemResponseDTO(cartItemShowDTOS);
    }

    @GetMapping("/cart/{customerId}")
    public CartItemResponseDTO getMyCart(@PathVariable long customerId){
        return new CartItemResponseDTO(orderService.getCartItems(customerId));
    }

    @DeleteMapping("/cart/{orderId}")
    public HttpStatus deleteOrder(@PathVariable long orderId){
        orderService.deleteOrder(orderId);
        return HttpStatus.NO_CONTENT;
    }
}
