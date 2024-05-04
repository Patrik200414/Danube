package com.danube.danube.controller;

import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
