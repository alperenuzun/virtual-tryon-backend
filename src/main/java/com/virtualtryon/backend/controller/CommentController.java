package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.model.Comment;
import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.model.User;
import com.virtualtryon.backend.payload.ApiResponse;
import com.virtualtryon.backend.payload.CommentAddRequest;
import com.virtualtryon.backend.repository.CommentRepository;
import com.virtualtryon.backend.repository.ProductRepository;
import com.virtualtryon.backend.repository.UserRepository;
import com.virtualtryon.backend.security.CurrentUser;
import com.virtualtryon.backend.security.UserPrincipal;
import com.virtualtryon.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{pId}")
    public ResponseEntity getComments(@PathVariable(name = "pId") Long productId) {
        return commentService.getComments(productId);
    }

    @PostMapping
    public ResponseEntity addComment(@CurrentUser UserPrincipal currentUser,
                                     @Valid @RequestBody CommentAddRequest commentAddRequest){
        return commentService.addComment(currentUser, commentAddRequest);
    }
}
