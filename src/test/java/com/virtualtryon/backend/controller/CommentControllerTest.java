package com.virtualtryon.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualtryon.backend.exception.AppException;
import com.virtualtryon.backend.model.Comment;
import com.virtualtryon.backend.payload.ApiResponse;
import com.virtualtryon.backend.payload.CommentAddRequest;
import com.virtualtryon.backend.payload.JwtAuthenticationResponse;
import com.virtualtryon.backend.payload.LoginRequest;
import com.virtualtryon.backend.service.CommentService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CommentControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    private final static String baseUrl = "/api/comment";

    private final static String authUrl = "/api/auth/signin";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    public void cleanup(){
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    void getComments_whenValidInput_thenReceiveOK() {
        //given
        long productId = 1L;

        ResponseEntity<List<Comment>> response = testRestTemplate.exchange(baseUrl + "/" + productId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getComments_whenInValidInput_thenReceiveBadRequest() {
        //given
        String productId = "testId";

        ResponseEntity<Object> response = testRestTemplate.exchange(baseUrl + "/" + productId, HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addComment_whenIsValidInputAndUserIsAuthorized_receiveOk() throws NoSuchFieldException {
        CommentAddRequest commentAddRequest = new CommentAddRequest();
        commentAddRequest.setProductId(1L);
        commentAddRequest.setComment("unit test comment!");
        commentAddRequest.setStar(3);

        String token = getToken("alperenuzun","123456");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        headers.set("Content-Type","application/json");

        HttpEntity<CommentAddRequest> request = new HttpEntity<>(commentAddRequest,headers);

        ResponseEntity<ApiResponse> response = testRestTemplate.postForEntity(baseUrl, request, ApiResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addComment_whenIsValidInputAndUserIsUnauthorized_receiveUnauthorized() {
        CommentAddRequest commentAddRequest = new CommentAddRequest();
        commentAddRequest.setProductId(1L);
        commentAddRequest.setComment("unit test comment!");
        commentAddRequest.setStar(3);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(baseUrl,commentAddRequest, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private String getToken(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest,headers);

        ResponseEntity<LinkedHashMap> response = testRestTemplate.postForEntity(authUrl, request, LinkedHashMap.class);
        LinkedHashMap linkedHashMap = response.getBody();
        assert linkedHashMap != null;
        String token = (String) linkedHashMap.get("accessToken");
        return token;
    }
}