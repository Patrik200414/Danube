package com.danube.danube.controller;

import com.danube.danube.model.dto.order.*;
import com.danube.danube.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PatchMapping("")
    public HttpStatus addShippingInformation(@RequestBody OrderInformationDTO orderInformationDTO){
        orderService.saveOrderInformation(orderInformationDTO);
        return HttpStatus.CREATED;
    }
}
