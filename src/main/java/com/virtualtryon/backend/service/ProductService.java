package com.virtualtryon.backend.service;

import com.virtualtryon.backend.exception.BadRequestException;
import com.virtualtryon.backend.model.Product;
import com.virtualtryon.backend.payload.ProductsRequest;
import com.virtualtryon.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getFilteredProducts(ProductsRequest productsRequest){
        List<Product> products = productRepository.findByGenderAndBrandInAndColorIn(productsRequest.getGender(), productsRequest.getBrand(), productsRequest.getColor());
        return products;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductDetail(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        if(!product.isPresent()) throw new BadRequestException("The productId could not be found!");
        return product.get();
    }

    public List<Product> getRecommendations(Long productId){
        // TODO: Read all the Id of products from the python file.
        List<Long> productIds = new ArrayList<>();
        productIds.add(3l);
        productIds.add(2l);
        productIds.add(productId);
        return productRepository.findByIdIn(productIds);
    }

}
