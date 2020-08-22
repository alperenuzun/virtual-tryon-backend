package com.virtualtryon.backend.payload;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class CommentAddRequest {
    @NotNull(message = "productId should be null")
    private Long productId;

    @NotBlank
    @Size(max = 255, message = "Comment size should not be more than 255!")
    private String comment;

    @NotNull
    @Min(value = 1, message = "Star must be between 1 and 5")
    @Max(value = 5, message = "Star must be between 1 and 5")
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

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStar(Integer star) {
        this.star = star;
    }
}
