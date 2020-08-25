package com.virtualtryon.backend.util;

import com.virtualtryon.backend.model.*;
import com.virtualtryon.backend.payload.CommentAddRequest;
import com.virtualtryon.backend.payload.LoginRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class TestUtil {

    private final static String CONTENT_TYPE = "application/json";
    private final static String username = "alperenuzun";
    private final static String password = "123456";
    private static Integer idxForCreateProduct = 1;

    public static LoginRequest getLoginRequest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }

    public static Product createProduct(Long id){
        Brand brand = createBrand();
        Form form = createProductForm();
        Color color = createColor();

        Product product = new Product();
        product.setId(id);
        product.setName("p1name");
        product.setSummary("p1summary");
        product.setDescription("p1desc");
        product.setObjName("p1obj");
        product.setImg("p1img");
        product.setBrand(brand);
        product.setProductCode("P1CODE");
        product.setGender((idxForCreateProduct % 2 == 0) ? 1 : 2);
        product.setPrice(320.0);
        product.setDiscount(0);
        product.setModelNo(123);
        product.setForm(form);
        product.setColor(color);

        idxForCreateProduct = (idxForCreateProduct == 3) ? 1 : ++idxForCreateProduct;

        return  product;
    }

    public static Color createColor(){
        Color color = new Color();
        color.setId((idxForCreateProduct % 2 == 0) ? 1L : 2L);
        color.setColor((idxForCreateProduct % 2 == 0) ? "black" : "blue");
        return color;
    }

    public static Form createProductForm(){
        Form form = new Form();
        form.setId(1L);
        form.setForm("Test form");
        return form;
    }

    public static Brand createBrand(){
        Brand brand = new Brand();
        brand.setId((idxForCreateProduct % 2 == 0) ? 1L : 2L);
        brand.setBrandName("BrandTest");
        brand.setImportance(1);
        return brand;
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
        return commentAddRequest;
    }

    public static <T> HttpEntity<T> getHttpEntityByToken(String token, T commentAddRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        headers.set(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);

        return new HttpEntity<>(commentAddRequest,headers);
    }

}
