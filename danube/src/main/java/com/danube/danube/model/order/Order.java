package com.danube.danube.model.order;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.user.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

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
    private int quantity;

    public Order() {
    }

    public Order(long id, UserEntity customer, Product product, boolean isOrdered, boolean isSent, int quantity) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.isOrdered = isOrdered;
        this.isSent = isSent;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Order order)) return false;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
