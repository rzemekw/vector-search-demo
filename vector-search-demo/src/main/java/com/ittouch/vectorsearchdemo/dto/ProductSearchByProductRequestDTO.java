package com.ittouch.vectorsearchdemo.dto;

import lombok.Data;

@Data
public class ProductSearchByProductRequestDTO {
    private Long productId;
    private boolean byDescription = true;
    private boolean byImage = true;
}
