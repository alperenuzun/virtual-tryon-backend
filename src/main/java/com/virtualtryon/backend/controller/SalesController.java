package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.model.*;
import com.virtualtryon.backend.payload.*;
import com.virtualtryon.backend.repository.*;
import com.virtualtryon.backend.security.CurrentUser;
import com.virtualtryon.backend.security.UserPrincipal;
import com.virtualtryon.backend.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping("/addSales")
    public ResponseEntity<ApiResponse> addSales(@CurrentUser UserPrincipal currentUser,
                                                @RequestBody SaleAddRequest saleAddRequest){
        return salesService.addSales(currentUser, saleAddRequest);
    }

    @PostMapping("/addCoupon/{code}")
    public ResponseEntity<ApiResponse> addCoupon(@CurrentUser UserPrincipal currentUser,
                                                 @PathVariable(name = "code") String code){
        return salesService.addCoupon(currentUser, code);
    }

    @GetMapping("/checkCoupon/{code}")
    public ResponseEntity<CheckCouponResponse> checkCoupons(@CurrentUser UserPrincipal currentUser,
                                                            @PathVariable(name = "code") String code) {
        return salesService.checkCoupons(currentUser, code);
    }

    @GetMapping("/coupons")
    public ResponseEntity<CouponResponse> getCoupons(@CurrentUser UserPrincipal currentUser) {
        return salesService.getCoupons(currentUser);
    }

}
