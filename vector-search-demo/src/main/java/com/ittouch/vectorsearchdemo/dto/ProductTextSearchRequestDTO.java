package com.ittouch.vectorsearchdemo.dto;

import lombok.Data;

@Data
public class ProductTextSearchRequestDTO {
    private String text;
    private boolean byDescription = true;
    private boolean byImage = true;
}
