package com.danube.danube.controller;

import com.danube.danube.model.dto.order.AddToCartDTO;
import com.danube.danube.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void addToCart(AddToCartDTO cartItem){

    }
}
