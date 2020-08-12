package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.model.Coupon;
import com.virtualtryon.backend.model.User;
import com.virtualtryon.backend.model.UserCoupon;
import com.virtualtryon.backend.payload.ApiResponse;
import com.virtualtryon.backend.payload.CheckCouponResponse;
import com.virtualtryon.backend.payload.CouponResponse;
import com.virtualtryon.backend.repository.CouponRepository;
import com.virtualtryon.backend.repository.SalesRepository;
import com.virtualtryon.backend.repository.UserCouponRepository;
import com.virtualtryon.backend.repository.UserRepository;
import com.virtualtryon.backend.security.CurrentUser;
import com.virtualtryon.backend.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final CouponRepository couponRepository;

    private final SalesRepository salesRepository;

    private final UserCouponRepository userCouponRepository;

    private final UserRepository userRepository;

    public SalesController(CouponRepository couponRepository, SalesRepository salesRepository, UserCouponRepository userCouponRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.salesRepository = salesRepository;
        this.userCouponRepository = userCouponRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/addCoupon")
    public ResponseEntity<ApiResponse> addCoupon(@CurrentUser UserPrincipal currentUser,
                                                 @PathVariable(name = "code") String code){
        try{
            Optional<User> user = userRepository.findById(currentUser.getId());
            Optional<Coupon> coupon = couponRepository.findByCouponCode(code);

            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUser(user.get());
            userCoupon.setCoupon(coupon.get());

            userCouponRepository.save(userCoupon);

            salesRepository.findByUserId(currentUser.getId()).stream().map(sale -> {
                sale.setAds(0);
                return salesRepository.save(sale);
            });
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(false, "Unable to add"));
        }

        return ResponseEntity.ok(new ApiResponse(true, "Successfully added"));
    }

    @GetMapping("/checkCoupon")
    public ResponseEntity<CheckCouponResponse> checkCoupons(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable(name = "code") String code) {
        Boolean couponExists = couponRepository.existsByCouponCode(code);
        Integer checkUnUsed = userCouponRepository.countByUserAndCouponAndUsed(currentUser.getId(), code);

        CheckCouponResponse checkCouponResponse = new CheckCouponResponse();

        if(couponExists && checkUnUsed > 0){
            Integer discount = couponRepository.getDiscountByCouponCode(code);
            userCouponRepository.findByUserAndCoupon(currentUser.getId(), code)
                    .stream().map(userCoupon -> {
                        userCoupon.setUsed(1);
                        return userCouponRepository.save(userCoupon);
                    }
            );

            checkCouponResponse.setDiscount(discount);
            checkCouponResponse.setSuccess(true);
        }

        return ResponseEntity.ok(checkCouponResponse);
    }

    @GetMapping("/coupons")
    public ResponseEntity<CouponResponse> getCoupons(@CurrentUser UserPrincipal currentUser) {
        Integer[] stages = new Integer[]{0,1,3,5,8,9,10};
        CouponResponse couponResponse = new CouponResponse();
        Boolean ads = salesRepository.existsByAds(1);

        if(ads){
            Long sales = salesRepository.countByUser(currentUser.getId());

            if(sales > 0)
                couponResponse.setShow(true);

            int start, end, stageIdx = 0;
            for (int i = 0; i < stages.length; i++){
                if(stages[i] <= sales)
                    stageIdx = i;
            }
            start = stages[stageIdx - 1] + 1;
            end = stages[stageIdx];

            List<Coupon> coupon = couponRepository.findCouponByStageBetween(start, end);
            couponResponse.setCoupon(coupon);
        }

        return ResponseEntity.ok(couponResponse);
    }

}
