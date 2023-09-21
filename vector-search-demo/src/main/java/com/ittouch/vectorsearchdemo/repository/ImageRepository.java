package com.ittouch.vectorsearchdemo.repository;

import com.ittouch.vectorsearchdemo.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
