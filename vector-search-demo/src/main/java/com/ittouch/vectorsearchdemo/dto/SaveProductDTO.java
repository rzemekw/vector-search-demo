package com.ittouch.vectorsearchdemo.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class SaveProductDTO {
    private Long id;
    private String description;

    @ToString.Exclude
    private byte[] image;
    private String imageFilename;
}
