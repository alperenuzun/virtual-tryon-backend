package com.virtualtryon.backend.repository;

import com.virtualtryon.backend.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("Select c from Coupon c Where c.stage between :start and :end")
    List<Coupon> findCouponByStageBetween(@Param("start") Integer start, @Param("end") Integer end);

    Boolean existsByCouponCode(String couponCode);

    @Query("Select c.discountPercentage from Coupon c Where c.couponCode=:couponCode")
    Integer getDiscountByCouponCode(@Param("couponCode") String couponCode);

    Optional<Coupon> findByCouponCode(String couponCode);
}
