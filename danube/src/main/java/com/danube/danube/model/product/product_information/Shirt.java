package com.danube.danube.model.product.product_information;

import jakarta.persistence.Entity;

@Entity
public class Shirt extends ProductInformation{
    private String size;

    public Shirt() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
