package com.ittouch.vectorsearchdemo.repository;

import com.ittouch.vectorsearchdemo.entity.VectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VectorRepository extends JpaRepository<VectorEntity, Long> {
}
