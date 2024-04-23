package com.danube.danube.model.product.connection;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.value.Value;
import jakarta.persistence.*;

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

    public String getDetailName(){
        return value.getDetailName();
    }
}
