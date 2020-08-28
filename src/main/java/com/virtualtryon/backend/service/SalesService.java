package com.virtualtryon.backend.service;

import com.virtualtryon.backend.model.*;
import com.virtualtryon.backend.payload.*;
import com.virtualtryon.backend.repository.*;
import com.virtualtryon.backend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalesService {
    @Autowired
    private CouponRepository couponRepository;
    
    @Autowired
    private SalesRepository salesRepository;
    
    @Autowired
    private UserCouponRepository userCouponRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private SalesDetailRepository salesDetailRepository;
    
    public ApiResponse addSales(Long userId, SaleAddRequest saleAddRequest){
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

            Optional<User> user = userRepository.findById(userId);
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
            return new ApiResponse(false, "Unable to add");
        }

        return new ApiResponse(true, "Successfully added");
    }

    public ApiResponse addCoupon(Long userId, String code){
        try{
            Optional<User> user = userRepository.findById(userId);
            Optional<Coupon> coupon = couponRepository.findByCouponCode(code);

            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUser(user.get());
            userCoupon.setCoupon(coupon.get());

            userCouponRepository.save(userCoupon);

            salesRepository.findByUserId(userId).stream().map(sale -> {
                sale.setAds(0);
                return salesRepository.save(sale);
            });
        }catch (Exception e){
            return new ApiResponse(false, "Unable to add");
        }

        return new ApiResponse(true, "Successfully added");
    }

    public CheckCouponResponse checkCoupons(Long userId, String code){
        CheckCouponResponse checkCouponResponse = new CheckCouponResponse();
        try{
            Boolean couponExists = couponRepository.existsByCouponCode(code);
            Integer checkUnUsed = userCouponRepository.countByUserAndCouponAndUsed(userId, code);

            if(couponExists && checkUnUsed > 0){
                Integer discount = couponRepository.getDiscountByCouponCode(code);
                userCouponRepository.findByUserAndCoupon(userId, code)
                        .stream().map(userCoupon -> {
                            userCoupon.setUsed(1);
                            return userCouponRepository.save(userCoupon);
                        }
                );

                checkCouponResponse.setDiscount(discount);
                checkCouponResponse.setSuccess(true);
            }
        }catch (Exception e){
            return checkCouponResponse;
        }

        return checkCouponResponse;
    }

    public CouponResponse getCoupons(Long userId){
        Integer[] stages = new Integer[]{0,1,3,5,8,9,10};
        CouponResponse couponResponse = new CouponResponse();
        Boolean ads = salesRepository.existsByAds(1);

        if(ads){
            Long sales = salesRepository.countByUser(userId);

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

        return couponResponse;
    }

}
