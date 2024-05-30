package com.danube.danube.repository.order;

import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(UserEntity customer);
    List<Order> findAllByProduct(Product product);
    @Query("SELECT o FROM Order o WHERE o.customer = :customer AND o.product = :product AND o.isOrdered = false")
    Optional<Order> findByCustomerAndProductAndIsOrderedFalse(UserEntity customer, Product product);
    @Query("SELECT o FROM Order o WHERE o.customer = :customer AND o.isOrdered = false")
    List<Order> findAllByCustomerIsOrderedFalse(UserEntity customer);
    List<Order> findAllByProductIsInAndCustomer(List<Product> products, UserEntity customer);
}
