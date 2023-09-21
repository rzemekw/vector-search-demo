package com.ittouch.vectorsearchdemo.service.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.KnnQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import com.ittouch.vectorsearchdemo.utils.ProductSearchConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchService {
    private final ElasticsearchClient esClient;

    public List<Long> searchByImage(float[] vector, int maxResults, Long idToExclude) {
        return performKnnSearch(
                List.of(getKnnQuery(vector, ProductSearchConstants.IMAGE_VECTOR_FIELD, maxResults, null, getIdFilterQuery(idToExclude))),
                maxResults
        );
    }

    public List<Long> searchByDescription(float[] vector, int maxResults, Long idToExclude) {
        return performKnnSearch(
                List.of(getKnnQuery(vector, ProductSearchConstants.DESCRIPTION_VECTOR_FIELD, maxResults, null,
                        getIdFilterQuery(idToExclude)
                )),
                maxResults
        );
    }

    public List<Long> searchByImageAndDescription(float[] vector, int maxResults) {
        return performKnnSearch(
                List.of(
                        getKnnQuery(vector, ProductSearchConstants.IMAGE_VECTOR_FIELD, maxResults, 0.5f),
                        getKnnQuery(vector, ProductSearchConstants.DESCRIPTION_VECTOR_FIELD, maxResults, 0.5f)
                ),
                maxResults
        );
    }

    public List<Long> searchByImageAndDescription(float[] imageVector, float[] descriptionVector, int maxResults, Long idToExclude) {
        return performKnnSearch(
                List.of(
                        getKnnQuery(imageVector, ProductSearchConstants.IMAGE_VECTOR_FIELD, maxResults, 0.5f,
                                getIdFilterQuery(idToExclude)),
                        getKnnQuery(descriptionVector, ProductSearchConstants.DESCRIPTION_VECTOR_FIELD, maxResults, 0.5f,
                                getIdFilterQuery(idToExclude))
                ),
                maxResults
        );
    }

    @SneakyThrows
    private List<Long> performKnnSearch(List<KnnQuery> knnQueries, int maxResults) {
        var request = getSearchRequest(knnQueries, maxResults);

        var response = esClient.search(request, Object.class);
        var hits = response.hits().hits();

        log.info("performKnnSearch, hits: {}", hits);

        return hits.stream().map(hit -> Long.parseLong(hit.id())).toList();
    }

    private SearchRequest getSearchRequest(List<KnnQuery> knnQueries, int maxResults) {
        return new SearchRequest.Builder()
                .index(ProductSearchConstants.INDEX_NAME)
                .knn(knnQueries)
                .size(maxResults)
                .source(new SourceConfig.Builder().fetch(false).build())
                .build();
    }

    private KnnQuery getKnnQuery(float[] vector, String fieldName, int maxResults, Float boost) {
        return getKnnQuery(vector, fieldName, maxResults, boost, null);
    }

    private KnnQuery getKnnQuery(float[] vector, String fieldName, int maxResults, Float boost, Query filterQuery) {
        var builder = new KnnQuery.Builder()
                .field(fieldName)
                .queryVector(toList(vector))
                .k(maxResults)
                .numCandidates(Math.max(100, maxResults * 4))
                .boost(boost);
        if (boost != null) {
            builder.boost(boost);
        }
        if (filterQuery != null) {
            builder.filter(filterQuery);
        }
        return builder.build();
    }

    private Query getIdFilterQuery(Long id) {
        if (id == null) {
            return null;
        }
        return new Query.Builder()
                .bool(b -> b
                        .mustNot(m -> m
                                .term(t -> t
                                        .field(ProductSearchConstants.ID_FIELD).value(id.toString())
                                )
                        )
                )
                .build();
    }

    private List<Float> toList(float[] array) {
        List<Float> floatList = new ArrayList<>();

        for (float f : array) {
            floatList.add(f);
        }

        return floatList;
    }
}
