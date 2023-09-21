package com.ittouch.vectorsearchdemo.service;

import com.ittouch.vectorsearchdemo.dto.ProductDTO;
import com.ittouch.vectorsearchdemo.dto.ProductSearchByProductRequestDTO;
import com.ittouch.vectorsearchdemo.dto.ProductTextSearchRequestDTO;
import com.ittouch.vectorsearchdemo.dto.SaveProductDTO;
import com.ittouch.vectorsearchdemo.service.elastic.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductPersistenceService productPersistenceService;
    private final VectorisationService vectorisationService;
    private final ProductSearchService productSearchService;

    public ProductDTO get(Long id) {
        log.info("in get for {}", id);

        var result = productPersistenceService.get(id);

        log.info("out get, result: {}", result);

        return result;
    }

    public ProductDTO create(SaveProductDTO product) {
        log.info("in create for {}", product);

        var result = productPersistenceService.create(product);
        updateVectors(result.getId(), product, false);

        log.info("out create, result: {}", result);

        return result;
    }

    public ProductDTO update(SaveProductDTO product) {
        log.info("in update for {}", product);

        var result = productPersistenceService.update(product);
        updateVectors(result.getId(), product, true);

        log.info("out update, result: {}", result);

        return result;
    }

    private void updateVectors(Long productId, SaveProductDTO product, boolean preserveImageVector) {
        log.info("in updateVectors for {}", productId);

        float[] descriptionVector;
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            descriptionVector = null;
        } else {
            descriptionVector = vectorisationService.vectorizeText(List.of(product.getDescription()))[0];
        }

        float[] imageVector;
        if (product.getImage() == null) {
            imageVector = null;
        } else {
            imageVector = vectorisationService.vectorizeImage(List.of(product.getImage()))[0];
        }

        productPersistenceService.setProductVectors(productId, descriptionVector, imageVector, preserveImageVector);

        log.info("out updateVectors for {}", productId);
    }

    public List<ProductDTO> searchByText(ProductTextSearchRequestDTO requestDTO) {
        log.info("in search for {}", requestDTO);

        if (requestDTO.getText() == null) {
            return productPersistenceService.getAll();
        }

        var vector = vectorisationService.vectorizeText(List.of(requestDTO.getText()))[0];

        if (requestDTO.isByDescription() && requestDTO.isByImage()) {
            log.info("search for {}, isByDescription, isByImage", requestDTO);
            var ids = productSearchService.searchByImageAndDescription(vector, 25);
            var result = productPersistenceService.getByIds(ids);
            log.info("out search for {}", requestDTO);
            return result;
        }

        if (requestDTO.isByDescription()) {
            log.info("search for {}, isByDescription", requestDTO);
            var ids = productSearchService.searchByDescription(vector, 25, null);
            var result = productPersistenceService.getByIds(ids);
            log.info("out search for {}", requestDTO);
            return result;
        }

        if (requestDTO.isByImage()) {
            log.info("search for {}, isByImage", requestDTO);
            var ids = productSearchService.searchByImage(vector, 25, null);
            var result = productPersistenceService.getByIds(ids);
            log.info("out search for {}", requestDTO);
            return result;
        }

        throw new IllegalArgumentException();
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchByProduct(ProductSearchByProductRequestDTO requestDTO) {
        log.info("in search for {}", requestDTO);

        var productVectors = productPersistenceService.getProductVectors(requestDTO.getProductId());
        var descriptionVector = productVectors.getDescriptionVector();
        var imageVector = productVectors.getImageVector();

        boolean isByDescription = requestDTO.isByDescription() && descriptionVector != null;
        boolean isByImage = requestDTO.isByImage() && imageVector != null;

        if (isByDescription && isByImage) {
            log.info("search for {}, isByDescription, isByImage", requestDTO);
            var ids = productSearchService.searchByImageAndDescription(imageVector, descriptionVector, 25, requestDTO.getProductId());
            var result = productPersistenceService.getByIds(ids);
            log.info("out search for {}", requestDTO);
            return result;
        }

        if (isByDescription) {
            log.info("search for {}, isByDescription", requestDTO);
            var ids = productSearchService.searchByDescription(descriptionVector, 25, requestDTO.getProductId());
            var result = productPersistenceService.getByIds(ids);
            log.info("out search for {}", requestDTO);
            return result;
        }

        if (isByImage) {
            log.info("search for {}, isByImage", requestDTO);
            var ids = productSearchService.searchByImage(imageVector, 25, requestDTO.getProductId());
            var result = productPersistenceService.getByIds(ids);
            log.info("out search for {}", requestDTO);
            return result;
        }

        throw new IllegalArgumentException();
    }
}
