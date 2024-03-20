package com.danube.danube.model.product.subcategory;

import com.danube.danube.model.product.connection.CategorySubcategory;
import com.danube.danube.model.product.detail.Detail;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "subcategory")
    private List<CategorySubcategory> categorySubcategories;
    @OneToMany(mappedBy = "subcategory")
    private List<Detail> details;

    public Subcategory() {
    }

    public Subcategory(long id, String name, List<CategorySubcategory> categorySubcategories, List<Detail> details) {
        this.id = id;
        this.name = name;
        this.categorySubcategories = categorySubcategories;
        this.details = details;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategorySubcategory> getCategorySubcategories() {
        return categorySubcategories;
    }

    public void setCategorySubcategories(List<CategorySubcategory> categorySubcategories) {
        this.categorySubcategories = categorySubcategories;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
