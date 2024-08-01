package com.danube.danube.model.product.connection;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.value.Value;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class ProductValue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Value value;

    public ProductValue() {
    }

    public ProductValue(long id, Product product, Value value) {
        this.id = id;
        this.product = product;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getValueName(){
        return value.getValue();
    }

    public String getDetailName(){return value.getDetailName();}
    public long getDetailId(){
        return value.getDetail().getId();
    }

    public long getValueId(){
        return value.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductValue that = (ProductValue) o;
        return id == that.id && Objects.equals(product, that.product) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, value);
    }
}
