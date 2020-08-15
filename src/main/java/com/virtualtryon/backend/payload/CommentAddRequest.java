package com.virtualtryon.backend.payload;

public class CommentAddRequest {
    private Long productId;

    private String comment;

    private Integer star;

    public Long getProductId() {
        return productId;
    }

    public String getComment() {
        return comment;
    }

    public Integer getStar() {
        return star;
    }

}
