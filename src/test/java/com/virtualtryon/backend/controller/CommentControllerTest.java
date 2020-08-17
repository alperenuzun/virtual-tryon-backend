package com.virtualtryon.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualtryon.backend.model.Comment;
import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.model.User;
import com.virtualtryon.backend.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    private final static String CONTENT_TYPE = "application/json";

    private final static String baseUrl = "/api/comment";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    void getComments_whenValidInput_thenReturns200() throws Exception {
        //given
        long productId = 1L;

        User user = new User();
        user.setId(1L);
        user.setEmail("a@a.com");
        user.setName("Alperen");
        user.setPassword("test");

        Product product = new Product();
        product.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("test comment");
        comment.setDatetime("2020-08-17 21:32:50");
        comment.setStar(3);
        comment.setUser(user);
        comment.setProduct(product);

        when(commentService.getComments(productId)).thenReturn(Collections.singletonList(comment));

        //when
        MvcResult mvcResult = mockMvc.perform(get(baseUrl+"/"+productId)
                .accept(CONTENT_TYPE)).andReturn();

        //then
        String responseBody = mvcResult.getResponse().getContentAsString();
        verify(commentService, times(1)).getComments(productId);
        assertThat(objectMapper.writeValueAsString(Collections.singletonList(comment)))
            .isEqualToIgnoringWhitespace(responseBody);
    }

    @Test
    void addComment() {
    }
}