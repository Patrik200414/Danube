package com.danube.danube.controller;

import com.danube.danube.model.dto.order.*;
import com.danube.danube.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final CartService orderService;

    @Autowired
    public OrderController(CartService orderService) {
        this.orderService = orderService;
    }

    @PatchMapping("")
    public void addShippingInformation(@RequestBody OrderInformationDTO orderInformationDTO){

    }
}
