package com.danube.danube.repository.product;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteImagesByProductAndFileNameIn(Product product, List<String> fileName);
}
