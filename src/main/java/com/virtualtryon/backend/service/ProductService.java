package com.virtualtryon.backend.service;

import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.payload.ProductsRequest;
import com.virtualtryon.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity getFilteredProducts(ProductsRequest productsRequest){
        List<Product> products = productRepository.findByGenderAndBrandInAndColorIn(productsRequest.getGender(), productsRequest.getBrand(), productsRequest.getColor());
        return ResponseEntity.ok(products);
    }

    public ResponseEntity getAllProducts(){
        return ResponseEntity.ok(productRepository.findAll());
    }

    public ResponseEntity getProductDetail(Long productId){
        return ResponseEntity.ok(productRepository.findById(productId));
    }

    public ResponseEntity getRecommendations(Long productId){
        // TODO: Read all the Id of products from the python file.
        List<Long> productIds = new ArrayList<>();
        productIds.add(3l);
        productIds.add(2l);
        productIds.add(productId);
        return ResponseEntity.ok(productRepository.findByIdIn(productIds));
    }

}
