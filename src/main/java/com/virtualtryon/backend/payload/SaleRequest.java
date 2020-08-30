package com.virtualtryon.backend.payload;

import javax.validation.constraints.NotNull;

public class SaleRequest {
    @NotNull
    private Long productId;

    @NotNull
    private Integer count;

    @NotNull
    private float price;

    private String couponCode;

    public Long getProductId() {
        return productId;
    }

    public Integer getCount() {
        return count;
    }

    public float getPrice() {
        return price;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
