package com.danube.danube.repository.product;

import com.danube.danube.model.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDetail, Long> {

}
