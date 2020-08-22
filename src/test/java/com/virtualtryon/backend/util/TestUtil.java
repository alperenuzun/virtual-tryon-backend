package com.virtualtryon.backend.util;

import com.virtualtryon.backend.model.Comment;
import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.model.User;
import com.virtualtryon.backend.payload.CommentAddRequest;
import com.virtualtryon.backend.payload.LoginRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class TestUtil {

    private final static String CONTENT_TYPE = "application/json";
    private final static String username = "alperenuzun";
    private final static String password = "123456";

    public static LoginRequest getLoginRequest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }

    public static Comment createValidComment(Long productId){
        User user = new User();
        user.setId(1L);
        user.setEmail("a@a.com");
        user.setName("Alperen");
        user.setPassword("test");

        Product product = new Product();
        product.setId(productId);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("test comment");
        comment.setDatetime("2020-08-17 21:32:50");
        comment.setStar(3);
        comment.setUser(user);
        comment.setProduct(product);

        return comment;
    }

    public static CommentAddRequest createValidAddRequest(){
        CommentAddRequest commentAddRequest = new CommentAddRequest();
        commentAddRequest.setProductId(1L);
        commentAddRequest.setComment("unit test comment!");
        commentAddRequest.setStar(3);
        return  commentAddRequest;
    }

    public static <T> HttpEntity<T> getHttpEntityByToken(String token, T commentAddRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        headers.set(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);

        return new HttpEntity<>(commentAddRequest,headers);
    }

}
