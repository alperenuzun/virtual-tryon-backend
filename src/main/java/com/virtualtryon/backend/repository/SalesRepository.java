package com.virtualtryon.backend.repository;

import com.virtualtryon.backend.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sale, Long> {

    @Query("Select count(s.id) from Sale s where s.user.id=:userId")
    Long countByUser(@Param("userId") Long userId);

    Boolean existsByAds(Integer ads);

    @Query("Select s from Sale s Where s.user.id=:userId")
    List<Sale> findByUserId(@Param("userId") Long userId);
}
