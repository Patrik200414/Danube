package com.danube.danube.model.product.detail;

import com.danube.danube.model.product.subcategory.Subcategory;
import com.danube.danube.model.product.value.Value;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private Subcategory subcategory;
    @OneToMany(mappedBy = "detail")
    private List<Value> values;

    public Detail() {
    }

    public Detail(long id, String name, Subcategory subcategory, List<Value> values) {
        this.id = id;
        this.name = name;
        this.subcategory = subcategory;
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

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }
}
