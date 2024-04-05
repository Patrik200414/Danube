package com.danube.danube.repository.product;

import com.danube.danube.model.product.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
