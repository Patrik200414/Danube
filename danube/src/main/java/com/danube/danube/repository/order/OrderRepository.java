package com.danube.danube.repository.order;

import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(UserEntity customer);
    List<Order> findAllByProduct(Product product);
    Optional<Order> findByCustomerAndProduct(UserEntity customer, Product product);
    List<Order> findAllByProductIsInAndCustomer(List<Product> products, UserEntity customer);
}
