package com.danube.danube.model.product.product_category;

import java.util.List;

public enum Category {
    CLOTHING(List.of(SubCategory.SHIRT));

    public final List<SubCategory> subCategories;

    Category(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
