package com.danube.danube.repository.product;

import com.danube.danube.model.product.subcategory.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Optional<Subcategory> findByName(String subcategoryName);
}
