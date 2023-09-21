package com.ittouch.vectorsearchdemo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ittouch.vectorsearchdemo.dto.ProductDTO;
import com.ittouch.vectorsearchdemo.dto.SaveProductDTO;
import com.ittouch.vectorsearchdemo.dto.SaveProductRequestDTO;
import com.ittouch.vectorsearchdemo.service.ProductService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class ProductController {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @SneakyThrows
    public ProductDTO create(@RequestParam(value = "file", required = false) MultipartFile file,
                             @RequestParam("data") String data
    ) {
        log.info("in create for data: {}, file: {}", data, file);

        var requestDTO = objectMapper.readValue(data, SaveProductRequestDTO.class);

        var dataDTO = new SaveProductDTO();
        dataDTO.setDescription(requestDTO.getDescription());
        dataDTO.setImage(file != null ? file.getBytes() : null);
        dataDTO.setImageFilename(file != null ? file.getOriginalFilename() : null);

        var result = productService.create(dataDTO);

        log.info("out create, result: {}", result);

        return result;
    }

    @PutMapping
    @SneakyThrows
    public ProductDTO update(@RequestParam(value = "file", required = false) MultipartFile file,
                             @RequestParam("data") String data
    ) {
        log.info("in update for data: {}, file: {}", data, file);

        var requestDTO = objectMapper.readValue(data, SaveProductRequestDTO.class);

        var dataDTO = new SaveProductDTO();
        dataDTO.setId(requestDTO.getId());
        dataDTO.setDescription(requestDTO.getDescription());
        dataDTO.setImage(file != null ? file.getBytes() : null);
        dataDTO.setImageFilename(file != null ? file.getOriginalFilename() : null);

        var result = productService.update(dataDTO);

        log.info("out update, result: {}", result);

        return result;
    }

    @GetMapping("/{id}")
    public ProductDTO get(@PathVariable Long id) {
        log.info("in get for {}", id);

        var result = productService.get(id);

        log.info("out get, result: {}", result);

        return result;
    }
}
