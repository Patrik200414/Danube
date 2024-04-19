package com.danube.danube.model.product.detail;

import com.danube.danube.model.product.connection.SubcategoryDetail;
import com.danube.danube.model.product.value.Value;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "detail")
    private List<SubcategoryDetail> subcategoryDetails;
    @OneToMany(mappedBy = "detail")
    private List<Value> values;

    public Detail() {
    }

    public Detail(long id, String name, List<SubcategoryDetail> subcategoryDetails, List<Value> values) {
        this.id = id;
        this.name = name;
        this.subcategoryDetails = subcategoryDetails;
        this.values = values;
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

    public List<SubcategoryDetail> getSubcategoryDetails() {
        return subcategoryDetails;
    }

    public void setSubcategoryDetails(List<SubcategoryDetail> subcategoryDetails) {
        this.subcategoryDetails = subcategoryDetails;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }
}
