package com.virtualtryon.backend.controller;

import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.payload.ProductsRequest;
import com.virtualtryon.backend.repository.ProductRepository;
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

    @PostMapping("/filter")
    public ResponseEntity getFilteredProducts(@Valid @RequestBody ProductsRequest productsRequest) {
        List<Product> products = productRepository.findByGenderAndBrandInAndColorIn(productsRequest.getGender(), productsRequest.getBrand(), productsRequest.getColor());
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{pId}")
    public ResponseEntity getProductDetail(@PathVariable(name = "pId") Long productId) {
        return ResponseEntity.ok(productRepository.findById(productId));
    }

    @GetMapping("/recommendation/{pId}")
    public ResponseEntity getRecommendations(@PathVariable(name = "pId") Long productId) {
        // TODO: Read all the Id of products from the python file.
        List<Long> productIds = new ArrayList<>();
        productIds.add(3l);
        productIds.add(2l);
        productIds.add(productId);
        return ResponseEntity.ok(productRepository.findByIdIn(productIds));
    }
}
