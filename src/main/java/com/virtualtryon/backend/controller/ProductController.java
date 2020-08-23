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
    private ProductService productService;

    @PostMapping("/filter")
    public ResponseEntity<List<Product>> getFilteredProducts(@Valid @RequestBody ProductsRequest productsRequest) {
        return ResponseEntity.ok(productService.getFilteredProducts(productsRequest));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{pId}")
    public ResponseEntity<Product> getProductDetail(@PathVariable(name = "pId") Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }

    @GetMapping("/recommendation/{pId}")
    public ResponseEntity<List<Product>> getRecommendations(@PathVariable(name = "pId") Long productId) {
        return ResponseEntity.ok(productService.getRecommendations(productId));
    }
}
