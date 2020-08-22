package com.virtualtryon.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualtryon.backend.exception.ApiError;
import com.virtualtryon.backend.exception.AppException;
import com.virtualtryon.backend.model.Comment;
import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.model.User;
import com.virtualtryon.backend.payload.ApiResponse;
import com.virtualtryon.backend.payload.CommentAddRequest;
import com.virtualtryon.backend.payload.JwtAuthenticationResponse;
import com.virtualtryon.backend.payload.LoginRequest;
import com.virtualtryon.backend.service.CommentService;
import com.virtualtryon.backend.util.TestUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void cleanup(){
        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    void getComments_whenValidInput_thenReceiveOK() {
        //given
        long productId = 1L;

        //when
        ResponseEntity<List<Comment>> response = getComments(baseUrl + "/" + productId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getComments_whenInValidInput_thenReceiveBadRequest() {
        //given
        String productId = "testId";

        //when
        ResponseEntity<Object> response = getComments(baseUrl + "/" + productId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getComment_whenIsValidInput_receiveResults() throws JsonProcessingException {
        //given
        long productId = 1L;

        Comment comment = TestUtil.createValidComment(productId);
        Mockito.when(commentService.getComments(productId))
                .thenReturn(Arrays.asList(comment));

        //when
        //TODO: check why it doesn't work with getComments function!
        ResponseEntity<String> response = testRestTemplate.exchange(baseUrl + "/" + productId, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});

        //then
        String responseBody = response.getBody();
        Mockito.verify(commentService, Mockito.times(1)).getComments(productId);
        assertThat(objectMapper.writeValueAsString(Arrays.asList(comment)))
                .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    public void addComment_whenIsValidInputAndUserIsAuthorized_receiveOk() {
        //given
        CommentAddRequest commentAddRequest = TestUtil.createValidAddRequest();

        //when
        String token = getAuthToken();
        HttpEntity<CommentAddRequest> request = TestUtil.getHttpEntityByToken(token,commentAddRequest);

        //then
        ResponseEntity<ApiResponse> response = testRestTemplate.postForEntity(baseUrl, request, ApiResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addComment_whenIsValidInputAndUserIsUnauthorized_receiveUnauthorized() {
        //given
        CommentAddRequest commentAddRequest = TestUtil.createValidAddRequest();

        //when
        ResponseEntity<Object> response = testRestTemplate.postForEntity(baseUrl,commentAddRequest, Object.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void addComment_whenCommentLengthIsMoreThan255Characters_receiveBadRequestAndMessage(){
        //given
        String veryLongString = IntStream.rangeClosed(1, 260).mapToObj(i -> "x").collect(Collectors.joining());
        CommentAddRequest commentAddRequest = TestUtil.createValidAddRequest();
        commentAddRequest.setComment(veryLongString);

        //when
        String token = getAuthToken();
        HttpEntity<CommentAddRequest> request = TestUtil.getHttpEntityByToken(token,commentAddRequest);

        //then
        ResponseEntity<ApiError> response = testRestTemplate.postForEntity(baseUrl, request, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertThat(validationErrors.get("comment")).isEqualTo("Comment size should not be more than 255!");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void addComment_whenCommentStarIsInvalid_receiveBadRequestAndMessage(){
        //given
        int star = 6;
        CommentAddRequest commentAddRequest = TestUtil.createValidAddRequest();
        commentAddRequest.setStar(star);

        //when
        String token = getAuthToken();
        HttpEntity<CommentAddRequest> request = TestUtil.getHttpEntityByToken(token,commentAddRequest);

        //then
        ResponseEntity<ApiError> response = testRestTemplate.postForEntity(baseUrl, request, ApiError.class);
        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertThat(validationErrors.get("star")).isEqualTo("Star must be between 1 and 5");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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

    public <T> ResponseEntity<T> getComments(String url){
        return testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<T>() {});
    }

}