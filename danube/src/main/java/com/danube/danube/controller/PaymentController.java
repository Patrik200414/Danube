package com.danube.danube.controller;

import com.danube.danube.model.dto.payment.PaymentNavigationDTO;
import com.danube.danube.model.dto.payment.PaymentUserIdDTO;
import com.danube.danube.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("")
    public PaymentNavigationDTO createPaymentSession(@RequestBody PaymentUserIdDTO paymentUserIdDTO) throws StripeException {
        Session paymentSession = paymentService.createPaymentSession(paymentUserIdDTO.userId());
        return new PaymentNavigationDTO(paymentSession.getUrl(), paymentSession.getId());
    }
}
