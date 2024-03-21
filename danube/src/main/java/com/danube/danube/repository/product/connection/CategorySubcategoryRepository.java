package com.danube.danube.repository.product.connection;

import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.connection.CategorySubcategory;
import com.danube.danube.model.product.subcategory.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorySubcategoryRepository extends JpaRepository<CategorySubcategory, Long> {
    List<CategorySubcategory> findAllByCategory(Category category);
    List<CategorySubcategory> findAllBySubcategory(Subcategory subcategory);
}
