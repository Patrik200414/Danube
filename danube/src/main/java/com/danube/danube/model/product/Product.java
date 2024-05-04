package com.danube.danube.model.product;

import com.danube.danube.model.order.Order;
import com.danube.danube.model.product.connection.ProductValue;
import com.danube.danube.model.product.image.Image;
import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.user.UserEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @OneToMany(mappedBy = "product")
    private List<ProductValue> productValues;
    @Column(nullable = false)
    private String productName;
    private double price;
    @Column(nullable = false)
    private String brand;
    private int quantity;
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;
    private double shippingPrice;
    private int deliveryTimeInDay;
    private int rating;
    private int sold;
    @ManyToOne
    private UserEntity seller;
    @OneToMany(mappedBy = "product")
    private List<Image> images;

    @ManyToOne
    public Subcategory subcategory;
    @OneToMany(mappedBy = "product")
    private List<Order> orders;

    public Product() {
    }

    public Product(long id, List<ProductValue> productValues, String productName, double price, String brand, int quantity, String description, double shippingPrice, int deliveryTimeInDay, int rating, int sold, UserEntity seller, List<Image> images, Subcategory subcategory, List<Order> orders) {
        this.id = id;
        this.productValues = productValues;
        this.productName = productName;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.description = description;
        this.shippingPrice = shippingPrice;
        this.deliveryTimeInDay = deliveryTimeInDay;
        this.rating = rating;
        this.sold = sold;
        this.seller = seller;
        this.images = images;
        this.subcategory = subcategory;
        this.orders = orders;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ProductValue> getProductValues() {
        return productValues;
    }

    public void setProductValues(List<ProductValue> productValues) {
        this.productValues = productValues;
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

    public UserEntity getSeller() {
        return seller;
    }

    public void setSeller(UserEntity seller) {
        this.seller = seller;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
    public String getSellerFullName(){
        return seller.getFullName();
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
    public Image getFirstProductImage(){
        return images.get(0);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
