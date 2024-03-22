package com.danube.danube.repository.product;

import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.value.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValueRepository extends JpaRepository<Value, Long> {
    List<Value> findAllByDetail(Detail detail);
}
