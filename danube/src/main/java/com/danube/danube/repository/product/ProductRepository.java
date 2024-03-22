package com.danube.danube.repository.product;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(UserEntity seller);
}
