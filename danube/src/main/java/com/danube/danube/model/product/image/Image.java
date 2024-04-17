package com.danube.danube.model.product.image;

import com.danube.danube.model.product.Product;
import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String fileName;
    @ManyToOne
    private Product product;

    public Image() {
    }

    public Image(long id, String fileName, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.product = product;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
