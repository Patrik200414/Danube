package com.danube.danube.model.product;

import com.danube.danube.model.product.product_information.ProductInformation;
import com.danube.danube.model.user.UserEntity;
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
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;


    @OneToOne
    private ProductInformation productInformation;

    private double shippingPrice;
    @Column(nullable = false)
    private int deliveryTimeInDay;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private int sold;
    /*
    TODO
    private List<String> images;
    */

    @Column(nullable = false)
    @ManyToOne
    private UserEntity user;

    public ProductDetail() {
    }

    public ProductDetail(long id, String productName, double price, String brand, int quantity, String description, ProductInformation productInformation, double shippingPrice, int deliveryTimeInDay, int rating, int sold, UserEntity user) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.description = description;
        this.productInformation = productInformation;
        this.shippingPrice = shippingPrice;
        this.deliveryTimeInDay = deliveryTimeInDay;
        this.rating = rating;
        this.sold = sold;
        this.user = user;
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

    public ProductInformation getProductInformation() {
        return productInformation;
    }

    public void setProductInformation(ProductInformation productInformation) {
        this.productInformation = productInformation;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
