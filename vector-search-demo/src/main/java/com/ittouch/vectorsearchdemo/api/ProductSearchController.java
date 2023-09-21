package com.ittouch.vectorsearchdemo.api;

import com.ittouch.vectorsearchdemo.dto.ProductDTO;
import com.ittouch.vectorsearchdemo.dto.ProductSearchByProductRequestDTO;
import com.ittouch.vectorsearchdemo.dto.ProductTextSearchRequestDTO;
import com.ittouch.vectorsearchdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products/search")
public class ProductSearchController {
    private final ProductService productService;

    @PostMapping("/text")
    public List<ProductDTO> searchByText(@RequestBody ProductTextSearchRequestDTO requestDTO) {
        return productService.searchByText(requestDTO);
    }

    @PostMapping("/product")
    public List<ProductDTO> searchByProduct(@RequestBody ProductSearchByProductRequestDTO requestDTO) {
        return productService.searchByProduct(requestDTO);
    }
}
