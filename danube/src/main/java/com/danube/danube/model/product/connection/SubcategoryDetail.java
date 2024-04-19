package com.danube.danube.model.product.connection;

import com.danube.danube.model.product.detail.Detail;
import com.danube.danube.model.product.subcategory.Subcategory;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class SubcategoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    private Subcategory subcategory;
    @ManyToOne
    private Detail detail;

    public SubcategoryDetail() {
    }

    public SubcategoryDetail(long id, Subcategory subcategory, Detail detail) {
        this.id = id;
        this.subcategory = subcategory;
        this.detail = detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }
}
