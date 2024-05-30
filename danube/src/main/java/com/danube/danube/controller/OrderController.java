package com.danube.danube.controller;

import com.danube.danube.model.dto.order.*;
import com.danube.danube.service.OrderService;
import com.danube.danube.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @Autowired
    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PatchMapping("")
    public HttpStatus addShippingInformation(@RequestBody OrderInformationDTO orderInformationDTO){
        orderService.saveOrderInformation(orderInformationDTO);
        return HttpStatus.CREATED;
    }

    @PatchMapping("/confirm")
    public HttpStatus confirmOrderAfterPayment(@RequestBody OrderSessionDTO orderSession) throws StripeException {
        boolean isPaid = paymentService.isPaid(orderSession.paymentSessionId());

        if (isPaid){
            orderService.setIsOrdered(orderSession.userId());
            return HttpStatus.CREATED;
        }

        return HttpStatus.PAYMENT_REQUIRED;
    }
}
