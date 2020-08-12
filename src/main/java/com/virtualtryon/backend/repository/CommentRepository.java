package com.virtualtryon.backend.repository;

import com.virtualtryon.backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c where c.product.id=:productId")
    List<Comment> findByProduct(@Param("productId") Long productId);

}
