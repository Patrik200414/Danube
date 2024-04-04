package com.danube.danube.model.product.image;

import com.danube.danube.model.product.Product;
import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String filePath;
    @ManyToOne
    private Product product;

    public Image() {
    }

    public Image(long id, String filePath, Product product) {
        this.id = id;
        this.filePath = filePath;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
