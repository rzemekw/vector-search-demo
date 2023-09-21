package com.ittouch.vectorsearchdemo.dto;

import lombok.Data;

@Data
public class ProductVectorsDTO {
    private float[] descriptionVector;
    private float[] imageVector;
}
