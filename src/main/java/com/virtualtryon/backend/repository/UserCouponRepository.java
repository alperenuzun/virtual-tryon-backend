package com.virtualtryon.backend.repository;

import com.virtualtryon.backend.model.Coupon;
import com.virtualtryon.backend.model.User;
import com.virtualtryon.backend.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Query("Select count(uc.id) from UserCoupon uc Where uc.user.id=:userId and uc.coupon.couponCode=:couponCode and uc.used=0")
    Integer countByUserAndCouponAndUsed(@Param("userId") Long userId, @Param("couponCode") String couponCode);

    @Query("Select uc from UserCoupon uc Where uc.user.id=:userId and uc.coupon.couponCode=:couponCode")
    List<UserCoupon> findByUserAndCoupon(@Param("userId") Long userId, @Param("couponCode") String couponCode);

}
