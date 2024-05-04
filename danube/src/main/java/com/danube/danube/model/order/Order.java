package com.danube.danube.model.order;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    private UserEntity customer;
    @ManyToOne
    private Product product;
    private boolean isOrdered;
    private boolean isSent;

    public Order() {
    }

    public Order(long id, UserEntity customer, Product product, boolean isOrdered, boolean isSent) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.isOrdered = isOrdered;
        this.isSent = isSent;
    }

    public long getId() {
        return id;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
