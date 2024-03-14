package com.danube.danube.model.product.product_information;

import com.danube.danube.model.product.ProductDetail;
import com.danube.danube.model.product.product_category.Category;
import com.danube.danube.model.product.product_enums.shirt.Gender;
import com.danube.danube.model.product.product_enums.shirt.NeckLine;
import com.danube.danube.model.product.product_enums.shirt.SleeveLength;
import jakarta.persistence.Entity;
import org.hibernate.engine.jdbc.Size;

@Entity
public class Shirt extends ProductInformation{
    
    private String style;
    private Gender gender;
    private Size size;
    private String fabric;
    private NeckLine neckLine;
    private SleeveLength sleeveLength;
    private String colors;

    public Shirt() {
    }

    public Shirt(long id, Category category, ProductDetail productDetail, String style, Gender gender, Size size, String fabric, NeckLine neckLine, SleeveLength sleeveLength, String colors) {
        super(id, category, productDetail);
        this.style = style;
        this.gender = gender;
        this.size = size;
        this.fabric = fabric;
        this.neckLine = neckLine;
        this.sleeveLength = sleeveLength;
        this.colors = colors;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public NeckLine getNeckLine() {
        return neckLine;
    }

    public void setNeckLine(NeckLine neckLine) {
        this.neckLine = neckLine;
    }

    public SleeveLength getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(SleeveLength sleeveLength) {
        this.sleeveLength = sleeveLength;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }
}
