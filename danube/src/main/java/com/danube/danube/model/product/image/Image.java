package com.danube.danube.model.product.image;

import com.danube.danube.model.product.Product;
import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String fileName;
    @Lob
    private byte[] imageFile;
    @ManyToOne
    private Product product;

    public Image() {
    }

    public Image(long id, String fileName, byte[] imageFile, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.imageFile = imageFile;
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

    public byte[] getImageFile() {
        return imageFile;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }
}
