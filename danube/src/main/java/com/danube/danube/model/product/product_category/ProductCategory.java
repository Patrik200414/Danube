package com.danube.danube.model.product.product_category;

import com.danube.danube.model.product.product_information.ProductInformation;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private Category category;

    @OneToMany(mappedBy = "productCategory")
    private List<ProductInformation> productInformations;

    public ProductCategory() {
    }

    public ProductCategory(long id, Category category, List<ProductInformation> productInformations) {
        this.id = id;
        this.category = category;
        this.productInformations = new ArrayList<>(productInformations);
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

    public List<ProductInformation> getProductInformations() {
        return new ArrayList<>(productInformations);
    }

    public void setProductInformations(List<ProductInformation> productInformations) {
        this.productInformations = new ArrayList<>(productInformations);
    }
}
