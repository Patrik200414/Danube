package com.danube.danube.model.product.connection;

import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.subcategory.Subcategory;
import jakarta.persistence.*;

@Entity
public class CategorySubcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Subcategory subcategory;

    public CategorySubcategory() {
    }

    public CategorySubcategory(long id, Category category, Subcategory subcategory) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
}
