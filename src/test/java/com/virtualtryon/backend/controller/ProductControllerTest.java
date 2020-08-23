package com.virtualtryon.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualtryon.backend.model.*;
import com.virtualtryon.backend.payload.LoginRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void getFilteredProducts() {
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
    void getProductDetail() {
    }

    @Test
    void getRecommendations() {
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

    public <T> ResponseEntity<T> getAllProducts(){
        return testRestTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<T>() {});
    }
}