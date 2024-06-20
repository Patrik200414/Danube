package com.danube.danube.repository.product;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.subcategory.Subcategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Optional<Subcategory> findByName(String subcategoryName);
    List<Subcategory> findAllByCategory(Category category);
    List<Subcategory> findByNameContainingIgnoreCaseOrderByNameAsc(String subcategoryName);
}
