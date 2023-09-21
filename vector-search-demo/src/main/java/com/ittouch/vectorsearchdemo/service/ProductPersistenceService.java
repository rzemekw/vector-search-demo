package com.ittouch.vectorsearchdemo.service;

import com.ittouch.vectorsearchdemo.dto.ProductDTO;
import com.ittouch.vectorsearchdemo.dto.ProductIndexDTO;
import com.ittouch.vectorsearchdemo.dto.ProductVectorsDTO;
import com.ittouch.vectorsearchdemo.dto.SaveProductDTO;
import com.ittouch.vectorsearchdemo.entity.ImageEntity;
import com.ittouch.vectorsearchdemo.entity.ProductEntity;
import com.ittouch.vectorsearchdemo.entity.VectorEntity;
import com.ittouch.vectorsearchdemo.repository.ImageRepository;
import com.ittouch.vectorsearchdemo.repository.ProductRepository;
import com.ittouch.vectorsearchdemo.repository.VectorRepository;
import com.ittouch.vectorsearchdemo.service.elastic.ProductIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductPersistenceService {
    private final ProductRepository productRepository;
    private final VectorRepository vectorRepository;
    private final ImageRepository imageRepository;
    private final ProductIndexService productIndexService;

    @Transactional(readOnly = true)
    public List<ProductDTO> getAll() {
        log.info("in getAll");

        var result = productRepository.findAll().stream()
                .map(this::toProductDTO)
                .toList();

        log.info("out getAll");

        return result;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getByIds(List<Long> ids) {
        log.info("in getByIds for {}", ids);

        var products = productRepository.findAllById(ids);
        var map = products.stream()
                .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        var result = ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .map(this::toProductDTO)
                .toList();

        log.info("out getByIds for {}", ids);

        return result;
    }

    @Transactional(readOnly = true)
    public ProductDTO get(Long id) {
        log.info("in get for {}", id);

        var result = productRepository.findById(id)
                .map(this::toProductDTO)
                .orElseThrow();

        log.info("out get for {}", result);

        return result;
    }

    @Transactional(readOnly = true)
    public ProductVectorsDTO getProductVectors(Long productId) {
        log.info("in getProductVectors for {}", productId);

        var productEntity = productRepository.findById(productId).orElseThrow();

        var descriptionVector = getVector(productEntity.getDescriptionVectorId());
        var imageVector = getVector(productEntity.getImageVectorId());

        var result = new ProductVectorsDTO();
        result.setDescriptionVector(descriptionVector);
        result.setImageVector(imageVector);

        log.info("out getProductVectors for {}", productId);

        return result;
    }

    @Transactional
    public ProductDTO create(SaveProductDTO product) {
        log.info("in create for {}", product);

        var productEntity = new ProductEntity();
        productEntity.setDescription(product.getDescription());

        if (product.getImage() != null) {
            var image = new ImageEntity();
            image.setData(product.getImage());
            image.setFilename(product.getImageFilename());
            productEntity.setImageId(imageRepository.save(image).getId());
        }

        var result = toProductDTO(productRepository.save(productEntity));

        log.info("out create for {}", result);

        return result;
    }

    @Transactional
    public ProductDTO update(SaveProductDTO product) {
        log.info("in update for {}", product);

        var productEntity = productRepository.findById(product.getId()).orElseThrow();
        productEntity.setDescription(product.getDescription());

        var prevImageId = productEntity.getImageId();

        if (product.getImage() != null) {
            var image = new ImageEntity();
            image.setData(product.getImage());
            image.setFilename(product.getImageFilename());
            productEntity.setImageId(imageRepository.save(image).getId());
            if (prevImageId != null) {
                imageRepository.deleteById(prevImageId);
            }
        }

        var result = toProductDTO(productRepository.save(productEntity));

        log.info("out update for {}", result);

        return result;
    }

    @Transactional
    public void setProductVectors(Long productId, float[] descriptionVector, float[] imageVector, boolean preserveImageVector) {
        log.info("in setProductVectors for {}", productId);

        var productEntity = productRepository.findById(productId).orElseThrow();

        if (imageVector != null) {
            productEntity.setImageVectorId(updateOrCreateVector(productEntity.getImageVectorId(), imageVector));
        }
        productEntity.setDescriptionVectorId(updateOrCreateVector(productEntity.getDescriptionVectorId(), descriptionVector));

        productRepository.save(productEntity);

        var indexModel = new ProductIndexDTO();
        indexModel.setId(productId);
        indexModel.setDescription(productEntity.getDescription());
        indexModel.setDescriptionVector(descriptionVector);
        if (imageVector != null) {
            indexModel.setImageVector(imageVector);
        } else if (preserveImageVector) {
            indexModel.setImageVector(getVector(productEntity.getImageVectorId()));
        }
        productIndexService.indexProduct(indexModel);

        log.info("out setProductVectors");
    }

    private Long updateOrCreateVector(Long vectorId, float[] vector) {
        log.info("in updateOrCreateVector for {}", vectorId);

        if (vectorId != null) {
            log.info("updateOrCreateVector for {}, vector exists", vectorId);
            var prevVector = vectorRepository.findById(vectorId).orElseThrow();

            if (vector == null) {
                vectorRepository.delete(prevVector);
                log.info("out updateOrCreateVector for {}, vector exists, vector is null, vector deleted", vectorId);
                return null;
            }

            prevVector.setData(vector);
            vectorRepository.save(prevVector);
            log.info("out updateOrCreateVector for {}, vector exists", vectorId);
            return vectorId;
        }

        if (vector == null) {
            log.info("out updateOrCreateVector for {}, vector does not exist, vector is null", vectorId);
            return null;
        }

        var newVector = vectorRepository.save(new VectorEntity(vector));
        log.info("out updateOrCreateVector for {}, vector does not exist, new vectorId", newVector.getId());
        return newVector.getId();
    }

    private ProductDTO toProductDTO(ProductEntity productEntity) {
        var result = new ProductDTO();
        result.setId(productEntity.getId());
        result.setDescription(productEntity.getDescription());
        result.setImageId(productEntity.getImageId());
        return result;
    }

    private float[] getVector(Long vectorId) {
        if (vectorId == null) {
            return null;
        }
        return vectorRepository.findById(vectorId).orElseThrow().getData();
    }
}
