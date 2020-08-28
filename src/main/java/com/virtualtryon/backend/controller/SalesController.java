package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.payload.*;
import com.virtualtryon.backend.security.CurrentUser;
import com.virtualtryon.backend.security.UserPrincipal;
import com.virtualtryon.backend.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping("/addSales")
    public ResponseEntity<ApiResponse> addSales(@CurrentUser UserPrincipal currentUser,
                                                @RequestBody SaleAddRequest saleAddRequest){
        return ResponseEntity.ok(salesService.addSales(currentUser.getId(), saleAddRequest));
    }

    @PostMapping("/addCoupon/{code}")
    public ResponseEntity<ApiResponse> addCoupon(@CurrentUser UserPrincipal currentUser,
                                                 @PathVariable(name = "code") String code){
        return ResponseEntity.ok(salesService.addCoupon(currentUser.getId(), code));
    }

    @GetMapping("/checkCoupon/{code}")
    public ResponseEntity<CheckCouponResponse> checkCoupons(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable(name = "code") String code) {
        return ResponseEntity.ok(salesService.checkCoupons(currentUser.getId(), code));
    }

    @GetMapping("/coupons")
    public ResponseEntity<CouponResponse> getCoupons(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(salesService.getCoupons(currentUser.getId()));
    }

}
