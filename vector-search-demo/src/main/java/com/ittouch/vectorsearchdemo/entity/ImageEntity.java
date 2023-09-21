package com.ittouch.vectorsearchdemo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(schema = "image", name = "image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data")
    private byte[] data;

    @Column(name = "filename")
    private String filename;
}
