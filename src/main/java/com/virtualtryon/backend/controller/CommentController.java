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
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{pId}")
    public ResponseEntity getComments(@PathVariable(name = "pId") Long productId) {
        return ResponseEntity.ok(commentRepository.findByProduct(productId));
    }

    @PostMapping
    public ResponseEntity addComment(@CurrentUser UserPrincipal currentUser,
                                     @Valid @RequestBody CommentAddRequest commentAddRequest){
        Optional<User> user = userRepository.findById(currentUser.getId());
        Optional<Product> product = productRepository.findById(commentAddRequest.getProductId());

        Instant now = Instant.now();
        String datetime = now.toString();
        datetime = datetime.substring(0,datetime.length()-5).replace("T"," ");

        Comment comment = new Comment();
        comment.setComment(commentAddRequest.getComment());
        comment.setStar(commentAddRequest.getStar());
        comment.setUser(user.get());
        comment.setProduct(product.get());
        comment.setDatetime(datetime);

        commentRepository.save(comment);
        return ResponseEntity.ok(new ApiResponse(true, "Comment Added Successfully"));
    }
}
