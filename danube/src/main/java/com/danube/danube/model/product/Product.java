package com.danube.danube.model.product;

import com.danube.danube.model.product.sub_category.SubCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String seller;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double shippingPrice;
    @Column(nullable = false)
    private int deliveryTimeInDay;

    @ManyToOne
    private SubCategory subCategory;

    //private List<String> images;


    public Product() {
    }

    public Product(long id, String productName, double price, String seller, String brand, int quantity, String description, double shippingPrice, int deliveryTimeInDay, SubCategory subCategory) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.seller = seller;
        this.brand = brand;
        this.quantity = quantity;
        this.description = description;
        this.shippingPrice = shippingPrice;
        this.deliveryTimeInDay = deliveryTimeInDay;
        this.subCategory = subCategory;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
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

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }
}
