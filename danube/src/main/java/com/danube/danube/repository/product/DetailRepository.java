package com.danube.danube.repository.product;

import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    Optional<Detail> findByName(String detailName);
    List<Detail> findBySubcategory(Subcategory subcategory);
}
