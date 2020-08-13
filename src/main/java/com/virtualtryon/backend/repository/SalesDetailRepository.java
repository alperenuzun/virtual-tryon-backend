package com.virtualtryon.backend.repository;

import com.virtualtryon.backend.model.SalesDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesDetailRepository extends JpaRepository<SalesDetail, Long> {

}
