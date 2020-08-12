package com.virtualtryon.backend.payload;

import com.virtualtryon.backend.model.Coupon;

import java.util.List;

public class CouponResponse {
    private Boolean show = false;
    private List<Coupon> coupon;

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public List<Coupon> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<Coupon> coupon) {
        this.coupon = coupon;
    }
}
