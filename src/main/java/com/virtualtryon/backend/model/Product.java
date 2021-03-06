package com.virtualtryon.backend.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String summary;

    @NotBlank
    private String description;

    @NotBlank
    private String objName;

    @NotBlank
    private String img;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @NotBlank
    private String productCode;

    private Integer gender;

    @NotBlank
    private Double price;

    private Integer discount;

    @NotBlank
    private Integer modelNo;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    private String size;

    private String glassMaterial;

    private String frameMaterial;

    private Integer polarized;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getModelNo() {
        return modelNo;
    }

    public void setModelNo(Integer modelNo) {
        this.modelNo = modelNo;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGlassMaterial() {
        return glassMaterial;
    }

    public void setGlassMaterial(String glassMaterial) {
        this.glassMaterial = glassMaterial;
    }

    public String getFrameMaterial() {
        return frameMaterial;
    }

    public void setFrameMaterial(String frameMaterial) {
        this.frameMaterial = frameMaterial;
    }

    public Integer getPolarized() {
        return polarized;
    }

    public void setPolarized(Integer polarized) {
        this.polarized = polarized;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
