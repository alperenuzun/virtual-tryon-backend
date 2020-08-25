package com.virtualtryon.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualtryon.backend.exception.ApiError;
import com.virtualtryon.backend.model.*;
import com.virtualtryon.backend.payload.LoginRequest;
import com.virtualtryon.backend.payload.ProductsRequest;
import com.virtualtryon.backend.service.ProductService;
import com.virtualtryon.backend.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.refEq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    private final static String baseUrl = "/api/product";

    private final static String authUrl = "/api/auth/signin";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void cleanup(){
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    void getFilteredProducts_whenValidInput_receiveOkAndResults() throws JsonProcessingException {
        //given
        Integer gender = 1;
        List<Long> brand = Arrays.asList(1L);
        List<Long> color = Arrays.asList(1L);

        ProductsRequest productsRequest = new ProductsRequest();
        productsRequest.setBrand(brand);
        productsRequest.setColor(color);
        productsRequest.setGender(gender);

        Product product1 = TestUtil.createProduct(1L);
        Product product2 = TestUtil.createProduct(2L);
        Product product3 = TestUtil.createProduct(3L);
        Product product4 = TestUtil.createProduct(4L);
        Product product5 = TestUtil.createProduct(5L);
        Mockito.when(productService.getFilteredProducts(refEq(productsRequest)))
                .thenReturn(Arrays.asList(product2,product5));

        //when
        ResponseEntity<String> response = testRestTemplate.postForEntity(baseUrl+"/filter", productsRequest, String.class);

        //then
        String responseBody = response.getBody();
        Mockito.verify(productService, Mockito.times(1)).getFilteredProducts(refEq(productsRequest));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(objectMapper.writeValueAsString(Arrays.asList(product2,product5)))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void getFilteredProducts_whenInValidGenderInput_receiveBadRequestAndMessage(){
        //given
        Integer gender = 3;
        List<Long> brand = Arrays.asList(1L);
        List<Long> color = Arrays.asList(1L);

        ProductsRequest productsRequest = new ProductsRequest();
        productsRequest.setBrand(brand);
        productsRequest.setColor(color);
        productsRequest.setGender(gender);

        //when
        ResponseEntity<ApiError> response = testRestTemplate.postForEntity(baseUrl+"/filter", productsRequest, ApiError.class);

        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertThat(validationErrors.get("gender")).isEqualTo("Gender value should be either 1 or 2!");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAllProducts_whenGetRequest_thenReceiveOK() {
        //when
        ResponseEntity<List<Product>> response = getAllProducts();

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllProducts_whenGetRequest_thenReceiveResults() throws JsonProcessingException {
        Product product1 = TestUtil.createProduct(1L);
        Product product2 = TestUtil.createProduct(2L);
        Mockito.when(productService.getAllProducts())
                .thenReturn(Arrays.asList(product1,product2));

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String responseBody = response.getBody();
        Mockito.verify(productService, Mockito.times(1)).getAllProducts();
        assertThat(objectMapper.writeValueAsString(Arrays.asList(product1,product2)))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void getProductDetail_whenIsValidInput_receiveOkAndResults() throws JsonProcessingException {
        //given
        long productId = 1L;

        Product product = TestUtil.createProduct(1L);
        Mockito.when(productService.getProductDetail(productId))
                .thenReturn(product);

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl+"/"+productId, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String responseBody = response.getBody();
        Mockito.verify(productService, Mockito.times(1)).getProductDetail(productId);
        assertThat(objectMapper.writeValueAsString(product))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void getProductDetail_whenInValidInput_thenReceiveBadRequest() {
        //given
        String productId = "testId";

        //when
        ResponseEntity<Object> response = testRestTemplate.exchange(baseUrl+"/"+productId, HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {});
        System.out.println(response.getBody());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getRecommendations_whenIsValidInput_receiveOkAndResults() throws JsonProcessingException {
        //given
        long productId = 1L;

        Product product1 = TestUtil.createProduct(1L);
        Product product2 = TestUtil.createProduct(2L);
        Product product3 = TestUtil.createProduct(3L);
        Mockito.when(productService.getRecommendations(productId))
                .thenReturn(Arrays.asList(product1,product2,product3));

        //when
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl+"/recommendation/"+productId, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String responseBody = response.getBody();
        Mockito.verify(productService, Mockito.times(1)).getRecommendations(productId);
        assertThat(objectMapper.writeValueAsString(Arrays.asList(product1,product2,product3)))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void getRecommendations_whenInValidInput_thenReceiveBadRequest() {
        //given
        String productId = "testId";

        //when
        ResponseEntity<Object> response = testRestTemplate.exchange(baseUrl+"/recommendation/"+productId, HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {});
        System.out.println(response.getBody());

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public <T> ResponseEntity<T> getAllProducts(){
        return testRestTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<T>() {});
    }
}