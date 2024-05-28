package com.danube.danube.service;

import com.danube.danube.custom_exception.login_registration.NonExistingUserException;
import com.danube.danube.custom_exception.payment.UnableToCreatePaymentSessionException;
import com.danube.danube.model.order.Order;
import com.danube.danube.model.user.UserEntity;
import com.danube.danube.repository.order.OrderRepository;
import com.danube.danube.repository.user.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.LineItem;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    @Value("${danube.app.payment.secret}")
    private String PAYMENT_SECRET;
    private final String PAGE_BASE_URL = "http://localhost:5173";
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    private Price createStripePrice(List<Order> orders, Product product) throws StripeException {
        Stripe.apiKey = PAYMENT_SECRET;

        double price = orders.stream()
                .mapToDouble(order -> order.getProduct().getPrice() + order.getProduct().getShippingPrice()).sum() * 100;

        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency("usd")
                .setActive(true)
                .setUnitAmount((long) Math.ceil(price))
                .setProduct(product.getId())
                .build();

        return Price.create(priceParams);
    }

    private Product createStripeProduct(List<Order> orders, String userEmail) throws StripeException {
        Stripe.apiKey = PAYMENT_SECRET;
        String paymentId = UUID.randomUUID().toString();
        List<String> orderedProductsName = orders.stream()
                .map(order -> order.getProduct().getProductName())
                .toList();

        ProductCreateParams productParams = ProductCreateParams.builder()
                .setId(paymentId)
                .setName(String.format("Danube-%s-%s", userEmail, paymentId))
                .setActive(true)
                .setShippable(true)
                .setDescription(String.format("Orders from Danube: %s", String.join(",", orderedProductsName)))
                .build();

        return Product.create(productParams);
    }

    private Session createSession(Price price, String customerEmail) throws StripeException {
        Stripe.apiKey = PAYMENT_SECRET;

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPrice(price.getId())
                .setQuantity(1L)
                .build();



        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(PAGE_BASE_URL + "/payment/success")
                .setCancelUrl(PAGE_BASE_URL + "/")
                .setCustomerEmail(customerEmail)
                .addLineItem(
                        lineItem
                ).build();



        return Session.create(params);
    }

    public Session createPaymentSession(long customerId) throws StripeException {
        Stripe.apiKey = PAYMENT_SECRET;
        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(NonExistingUserException::new);

        List<Order> ordersByCustomer = orderRepository.findAllByCustomer(customer);

        if(ordersByCustomer.isEmpty()){
            throw new UnableToCreatePaymentSessionException();
        }

        Product stripeProduct = createStripeProduct(ordersByCustomer, customer.getEmail());
        Price stripePrice = createStripePrice(ordersByCustomer, stripeProduct);
        return createSession(stripePrice, customer.getEmail());
    }
}
