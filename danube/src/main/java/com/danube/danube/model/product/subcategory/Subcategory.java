package com.danube.danube.model.product.subcategory;

import com.danube.danube.model.product.Product;
import com.danube.danube.model.product.category.Category;
import com.danube.danube.model.product.connection.SubcategoryDetail;
import com.danube.danube.model.product.detail.Detail;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false, unique = true)
    private String name;
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "subcategory")
    private List<SubcategoryDetail> subcategoryDetails;

    @OneToMany(mappedBy = "subcategory")
    private List<Product> products;

    public Subcategory() {
    }

    public Subcategory(long id, String name, Category category, List<SubcategoryDetail> subcategoryDetails, List<Product> products) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subcategoryDetails = subcategoryDetails;
        this.products = products;
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

    public List<SubcategoryDetail> getSubcategoryDetails() {
        return subcategoryDetails;
    }

    public void setSubcategoryDetails(List<SubcategoryDetail> subcategoryDetails) {
        this.subcategoryDetails = subcategoryDetails;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
