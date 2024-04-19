package com.danube.danube.repository.product.connection;

import com.danube.danube.model.product.connection.SubcategoryDetail;
import com.danube.danube.model.product.subcategory.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryDetailRepository extends JpaRepository<SubcategoryDetail, Long> {
    List<SubcategoryDetail> findAllBySubcategory(Subcategory subcategory);
}
