package com.danube.danube.controller;

import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.CartItemResponseDTO;
import com.danube.danube.model.dto.order.CartItemShowDTO;
import com.danube.danube.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/cart/{customerId}")
    public CartItemResponseDTO getMyCart(@PathVariable long customerId){
        return new CartItemResponseDTO(orderService.getCartItems(customerId));
    }
}
