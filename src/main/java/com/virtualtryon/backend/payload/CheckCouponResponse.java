package com.virtualtryon.backend.payload;

public class CheckCouponResponse {
    private Boolean success = false;
    private Integer discount = 0;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

}
