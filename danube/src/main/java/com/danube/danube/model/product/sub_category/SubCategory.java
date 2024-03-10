package com.danube.danube.model.product.sub_category;

import com.danube.danube.model.product.Product;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sub_categories")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    private String subCategoryName;
    @Column(nullable = false)
    private double productInfo;
    @OneToMany(mappedBy = "subCategory")
    private List<Product> product;

    public SubCategory() {
    }


    public SubCategory(long id, String subCategoryName, double productInfo, List<Product> product) {
        this.id = id;
        this.subCategoryName = subCategoryName;
        this.productInfo = productInfo;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public double getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(double productInfo) {
        this.productInfo = productInfo;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
