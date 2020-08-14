package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.model.*;
import com.virtualtryon.backend.payload.*;
import com.virtualtryon.backend.repository.*;
import com.virtualtryon.backend.security.CurrentUser;
import com.virtualtryon.backend.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final CouponRepository couponRepository;

    private final SalesRepository salesRepository;

    private final UserCouponRepository userCouponRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final SalesDetailRepository salesDetailRepository;

    public SalesController(CouponRepository couponRepository, SalesRepository salesRepository, UserCouponRepository userCouponRepository, UserRepository userRepository, ProductRepository productRepository, SalesDetailRepository salesDetailRepository) {
        this.couponRepository = couponRepository;
        this.salesRepository = salesRepository;
        this.userCouponRepository = userCouponRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.salesDetailRepository = salesDetailRepository;
    }

    @PostMapping("/addSales")
    public ResponseEntity<ApiResponse> addSales(@CurrentUser UserPrincipal currentUser,
                                                @RequestBody SaleAddRequest saleAddRequest){
        try{
            List<SaleRequest> shoppingCart = saleAddRequest.getSalesList();
            Integer totalCount = 0;
            String couponCode = "";
            float totalPrice = 0;

            for(SaleRequest saleRequest : shoppingCart){
                totalCount += saleRequest.getCount();
                totalPrice += saleRequest.getPrice();
                couponCode = saleRequest.getCouponCode();
            }

            Optional<User> user = userRepository.findById(currentUser.getId());
            Optional<Coupon> coupon = couponRepository.findByCouponCode(couponCode);

            Instant now = Instant.now();
            String datetime = now.toString();
            datetime = datetime.substring(0,datetime.length()-5).replace("T"," ");

            Sale sale = new Sale();
            sale.setCoupon(coupon.orElse(null));
            sale.setTotalAmount(totalPrice);
            sale.setTotalQuantity(totalCount);
            sale.setAds(1);
            sale.setDatetime(datetime);
            sale.setUser(user.get());

            Sale newSale = salesRepository.save(sale);

            List<SalesDetail> salesDetails = new ArrayList<>();
            for(SaleRequest saleRequest : shoppingCart){
                Optional<Product> product = productRepository.findById(saleRequest.getProductId());

                SalesDetail salesDetail = new SalesDetail();
                salesDetail.setPrice(saleRequest.getPrice());
                salesDetail.setProduct(product.get());
                salesDetail.setQuantity(saleRequest.getCount());
                salesDetail.setSale(newSale);

                salesDetails.add(salesDetail);
            }

            salesDetailRepository.saveAll(salesDetails);
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(false, "Unable to add"));
        }

        return ResponseEntity.ok(new ApiResponse(true, "Successfully added"));
    }

    @PostMapping("/addCoupon/{code}")
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

    @GetMapping("/checkCoupon/{code}")
    public ResponseEntity<CheckCouponResponse> checkCoupons(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable(name = "code") String code) {
        CheckCouponResponse checkCouponResponse = new CheckCouponResponse();
        try{
            Boolean couponExists = couponRepository.existsByCouponCode(code);
            Integer checkUnUsed = userCouponRepository.countByUserAndCouponAndUsed(currentUser.getId(), code);

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
        }catch (Exception e){
            return ResponseEntity.ok(checkCouponResponse);
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
