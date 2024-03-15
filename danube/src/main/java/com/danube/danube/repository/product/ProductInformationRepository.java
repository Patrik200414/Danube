package com.danube.danube.repository.product;

import com.danube.danube.model.product.product_information.ProductInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInformationRepository extends JpaRepository<ProductInformation, Long> {
}
