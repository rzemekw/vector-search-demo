package com.ittouch.vectorsearchdemo.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class ProductIndexDTO {
    private Long id;
    private String description;
    @ToString.Exclude
    private float[] descriptionVector;
    @ToString.Exclude
    private float[] imageVector;
}
