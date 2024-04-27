package com.danube.danube.repository.product;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySeller(UserEntity seller);
    List<Product> findBySubcategoryAndIdNotOrderBySoldDescRatingDesc(Subcategory subcategory, long id, Pageable pageable);
}
