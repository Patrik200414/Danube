package com.danube.danube.repository.product;

import com.danube.danube.model.product.detail.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    Optional<Detail> findByName(String detailName);
    List<Detail> findAllByNameIn(Set<String> detailNames);
}
