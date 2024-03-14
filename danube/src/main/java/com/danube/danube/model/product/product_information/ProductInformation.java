package com.danube.danube.model.product.product_information;

import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.ProductCategory;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ProductInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    private ProductCategory productCategory;
    @OneToOne
    private ProductDetail productDetail;

    public ProductInformation() {
    }

    public ProductInformation(long id, ProductCategory productCategory, ProductDetail productDetail) {
        this.id = id;
        this.productCategory = productCategory;
        this.productDetail = productDetail;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }
}
