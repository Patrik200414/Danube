package com.danube.danube.controller;

import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.model.dto.order.CartItemShowDTO;
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
    public HttpStatus addToCart(@RequestBody AddToCartDTO cartItem){
        orderService.addToCart(cartItem);
        return HttpStatus.CREATED;
    }

    @GetMapping("/cart/{customerId}")
    public List<CartItemShowDTO> getMyCart(@RequestParam long customerId){
        return orderService.getCartItems(customerId);
    }
}
