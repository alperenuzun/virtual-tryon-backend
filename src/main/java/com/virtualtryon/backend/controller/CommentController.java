package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.model.Comment;
import com.virtualtryon.backend.payload.ApiResponse;
import com.virtualtryon.backend.payload.CommentAddRequest;
import com.virtualtryon.backend.security.CurrentUser;
import com.virtualtryon.backend.security.UserPrincipal;
import com.virtualtryon.backend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@Validated
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{pId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable(name = "pId") Long productId) {
        return ResponseEntity.ok(commentService.getComments(productId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addComment(@CurrentUser UserPrincipal currentUser,
                                                  @Valid @RequestBody CommentAddRequest commentAddRequest){
        return commentService.addComment(currentUser, commentAddRequest);
    }
}
