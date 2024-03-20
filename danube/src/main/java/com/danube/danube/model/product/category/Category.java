package com.danube.danube.model.product.category;

import com.danube.danube.model.product.connection.CategorySubcategory;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<CategorySubcategory> categorySubcategories;

    public Category() {
    }

    public Category(long id, String name, List<CategorySubcategory> categorySubcategories) {
        this.id = id;
        this.name = name;
        this.categorySubcategories = categorySubcategories;
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
}
