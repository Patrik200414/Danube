package com.danube.danube.model.product;

import com.danube.danube.model.product.product_information.ProductInformation;
import jakarta.persistence.*;

@Entity

public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private String description;


    @OneToOne
    private ProductInformation productInformation;

    private double shippingPrice;
    @Column(nullable = false)
    private int deliveryTimeInDay;
    /*
    TODO
    private List<String> images;
    */

    public ProductDetail() {
    }

    public ProductDetail(long id, String productName, double price, String brand, int quantity, String description, double shippingPrice, int deliveryTimeInDay) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.description = description;
        this.shippingPrice = shippingPrice;
        this.deliveryTimeInDay = deliveryTimeInDay;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public int getDeliveryTimeInDay() {
        return deliveryTimeInDay;
    }

    public void setDeliveryTimeInDay(int deliveryTimeInDay) {
        this.deliveryTimeInDay = deliveryTimeInDay;
    }
}
