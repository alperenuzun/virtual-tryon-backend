package com.virtualtryon.backend.payload;


import java.util.List;

public class ProductsRequest {
    private Integer gender;
    private List<Long> brand;
    private List<Long> color;

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List<Long> getBrand() {
        return brand;
    }

    public void setBrand(List<Long> brand) {
        this.brand = brand;
    }

    public List<Long> getColor() {
        return color;
    }

    public void setColor(List<Long> color) {
        this.color = color;
    }
}
