package com.danube.danube.model.product.value;

import com.danube.danube.model.product.connection.ProductValue;
import com.danube.danube.model.product.detail.Detail;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Value {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String value;
    @ManyToOne
    private Detail detail;
    @OneToMany(mappedBy = "value")
    private List<ProductValue> productValues;

    public Value() {
    }

    public Value(long id, String value, Detail detail, List<ProductValue> productValues) {
        this.id = id;
        this.value = value;
        this.detail = detail;
        this.productValues = productValues;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public List<ProductValue> getProductValues() {
        return productValues;
    }

    public void setProductValues(List<ProductValue> productValues) {
        this.productValues = productValues;
    }

    public String getDetailName(){
        return detail.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return id == value1.id && Objects.equals(value, value1.value) && Objects.equals(detail, value1.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, detail);
    }
}
