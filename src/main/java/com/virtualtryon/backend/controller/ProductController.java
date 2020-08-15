package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.payload.ProductsRequest;
import com.virtualtryon.backend.repository.ProductRepository;
import com.virtualtryon.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @PostMapping("/filter")
    public ResponseEntity getFilteredProducts(@Valid @RequestBody ProductsRequest productsRequest) {
        return productService.getFilteredProducts(productsRequest);
    }

    @GetMapping
    public ResponseEntity getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{pId}")
    public ResponseEntity getProductDetail(@PathVariable(name = "pId") Long productId) {
        return productService.getProductDetail(productId);
    }

    @GetMapping("/recommendation/{pId}")
    public ResponseEntity getRecommendations(@PathVariable(name = "pId") Long productId) {
        return productService.getRecommendations(productId);
    }
}
