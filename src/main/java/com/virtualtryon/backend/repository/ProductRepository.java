package com.virtualtryon.backend.repository;

import com.virtualtryon.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p where p.brand.id in :brand and p.gender=:gender and p.color.id in :color")
    List<Product> findByGenderAndBrandInAndColorIn(@Param("gender") Integer gender, @Param("brand") List<Long> brand, @Param("color") List<Long> color);

    @Query("SELECT p FROM Product p where p.id in :productIds")
    List<Product> findByIdIn(@Param("productIds") List<Long> productIds);

    @Override
    Optional<Product> findById(Long aLong);
}
