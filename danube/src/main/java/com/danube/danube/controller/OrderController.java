package com.danube.danube.controller;

import com.danube.danube.model.dto.order.*;
import com.danube.danube.service.OrderService;
import com.danube.danube.service.PaymentService;
import com.danube.danube.utility.json.JsonUtility;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final JsonUtility jsonUtility;

    @Autowired
    public OrderController(OrderService orderService, PaymentService paymentService, JsonUtility jsonUtility) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.jsonUtility = jsonUtility;
    }

    @PatchMapping("")
    public HttpStatus addShippingInformation(@RequestBody String orderInformation) throws IOException {
        OrderInformationDTO orderInformationDTO = jsonUtility.validateJson(orderInformation, OrderInformationDTO.class);
        orderService.saveOrderInformation(orderInformationDTO);
        return HttpStatus.CREATED;
    }

    @PatchMapping("/confirm")
    public HttpStatus confirmOrderAfterPayment(@RequestBody OrderSessionDTO orderSession) throws StripeException {
        boolean isPaid = paymentService.isPaid(orderSession.paymentSessionId());

        if (isPaid){
            orderService.setIsOrdered(orderSession.userId(), LocalDateTime.now());
            return HttpStatus.CREATED;
        }

        return HttpStatus.PAYMENT_REQUIRED;
    }
}
