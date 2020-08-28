package com.virtualtryon.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualtryon.backend.model.Coupon;
import com.virtualtryon.backend.payload.CouponResponse;
import com.virtualtryon.backend.payload.LoginRequest;
import com.virtualtryon.backend.service.SalesService;
import com.virtualtryon.backend.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SalesControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    private final static String baseUrl = "/api/sales";

    private final static String authUrl = "/api/auth/signin";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SalesService salesService;

    @BeforeEach
    public void cleanup(){
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    void addSales() {
    }

    @Test
    void addCoupon() {
    }

    @Test
    void checkCoupons() {
    }

    @Test
    void getCoupons_whenValidUser_thenReceiveResults() throws JsonProcessingException {

        //given
        Long userId = 1L;

        Coupon coupon1 = new Coupon();
        coupon1.setDiscountPercentage(15);
        Coupon coupon2 = new Coupon();
        coupon2.setDiscountPercentage(16);
        Coupon coupon3 = new Coupon();
        coupon3.setDiscountPercentage(17);
        Coupon coupon4 = new Coupon();
        coupon4.setDiscountPercentage(18);
        Coupon coupon5 = new Coupon();
        coupon5.setDiscountPercentage(19);

        CouponResponse couponResponse = new CouponResponse();
        couponResponse.setShow(true);
        couponResponse.setCoupon(Arrays.asList(coupon1,coupon2,coupon3,coupon4,coupon5));

        Mockito.when(salesService.getCoupons(userId)).thenReturn(couponResponse);
        //handle with user mock

        //when
        String token = getAuthToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        headers.set(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);

        HttpEntity<Object> httpEntity = new HttpEntity<>(null,headers);
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl+"/coupons", HttpMethod.GET, httpEntity, String.class);

        //then
        String responseBody = response.getBody();
        Mockito.verify(salesService, Mockito.times(1)).getCoupons(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(objectMapper.writeValueAsString(couponResponse))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    private String getAuthToken() {
        LoginRequest loginRequest = TestUtil.getLoginRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE,CONTENT_TYPE);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest,headers);

        ResponseEntity<LinkedHashMap> response = testRestTemplate.postForEntity(authUrl, request, LinkedHashMap.class);
        LinkedHashMap linkedHashMap = response.getBody();
        assert linkedHashMap != null;
        return (String) linkedHashMap.get("accessToken");
    }
}