package com.ittouch.vectorsearchdemo.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.ittouch.vectorsearchdemo.dto.ProductIndexDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Request;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductIndexService {
    private final ElasticsearchClient esClient;

    @SneakyThrows
    public void indexProduct(ProductIndexDTO productIndexDTO) {
        log.info("in indexProduct for {}", productIndexDTO.getId());

        var response = esClient.index(i -> i
                .index("products")
                .id(productIndexDTO.getId().toString())
                .document(productIndexDTO)
        );

        log.info("in indexProduct for {}, version: {} ", productIndexDTO.getId(), response.version());
    }
}
