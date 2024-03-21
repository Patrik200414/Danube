package com.danube.danube.repository.product.connection;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.connection.ProductValue;
import com.danube.danube.model.product.value.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductValueRepository extends JpaRepository<ProductValue, Long> {
    List<ProductValue> findAllByProduct(Product product);
    List<ProductValue> findAllByValue(Value value);
}
