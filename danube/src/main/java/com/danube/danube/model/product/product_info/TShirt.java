package com.danube.danube.model.product.product_info;

import jakarta.persistence.*;


@Entity
@Table(name = "t_shirts")
public class TShirt{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false)
    private String fitType;
    @Column(nullable = false)
    private double sleeveLength;
    @Column(nullable = false)
    private boolean stretch;
    @Column(nullable = false)
    private String fabric;
    @Column(nullable = false)
    private String pattern;
    @Column(nullable = false)
    private String bodyFit;

    public TShirt() {
    }

    public TShirt(long id, String fitType, double sleeveLength, boolean stretch, String fabric, String pattern, String bodyFit) {
        this.id = id;
        this.fitType = fitType;
        this.sleeveLength = sleeveLength;
        this.stretch = stretch;
        this.fabric = fabric;
        this.pattern = pattern;
        this.bodyFit = bodyFit;
    }

    public String getFitType() {
        return fitType;
    }

    public void setFitType(String fitType) {
        this.fitType = fitType;
    }

    public double getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(double sleeveLength) {
        this.sleeveLength = sleeveLength;
    }

    public boolean isStretch() {
        return stretch;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getBodyFit() {
        return bodyFit;
    }

    public void setBodyFit(String bodyFit) {
        this.bodyFit = bodyFit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
