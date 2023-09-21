package com.ittouch.vectorsearchdemo.repository;

import com.ittouch.vectorsearchdemo.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
