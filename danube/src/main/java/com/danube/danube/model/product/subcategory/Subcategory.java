package com.danube.danube.model.product.subcategory;

import com.danube.danube.model.product.category.Category;
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
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "subcategory")
    private List<Detail> details;

    public Subcategory() {
    }

    public Subcategory(long id, String name, List<Detail> details) {
        this.id = id;
        this.name = name;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
