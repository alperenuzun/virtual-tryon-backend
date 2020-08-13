package com.virtualtryon.backend.payload;

public class SaleRequest {
    private Long productId;
    private Integer count;
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
}
