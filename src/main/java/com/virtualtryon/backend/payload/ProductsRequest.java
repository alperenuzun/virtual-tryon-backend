package com.virtualtryon.backend.payload;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ProductsRequest {
    @Max(2)
    private Integer gender;

    @NotNull
    private List<Long> brand;

    @NotNull
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
