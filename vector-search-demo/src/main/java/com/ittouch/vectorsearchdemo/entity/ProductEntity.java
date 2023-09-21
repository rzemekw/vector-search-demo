package com.ittouch.vectorsearchdemo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(schema = "product", name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_vector_id")
    private Long imageVectorId;

    @Column(name = "description_vector_id")
    private Long descriptionVectorId;

}
