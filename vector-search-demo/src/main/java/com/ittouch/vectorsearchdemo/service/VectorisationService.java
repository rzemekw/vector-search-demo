package com.ittouch.vectorsearchdemo.service;

import com.ittouch.vectorsearchdemo.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VectorisationService {
    private final RestTemplate restTemplate;

    @Value("${apps.vectorisationApp.url}")
    private String url;

    private String getImageUrl() {
        return url + "/images";
    }

    private String getTextUrl() {
        return url + "/texts";
    }

    public float[][] vectorizeText(List<String> text) {
        log.info("in vectorizeText for {}", text);

        var map = Map.of("texts", text);

        var result = restTemplate.postForObject(getTextUrl(), map, float[][].class);

        log.info("out vectorizeText");

        return result;
    }

    public float[][] vectorizeImage(List<byte[]> images) {
        log.info("in vectorizeImage");

        var map = new LinkedMultiValueMap<String, Object>();

        images.stream()
                .map(r -> FileUtils.fromByteArray(r, "image.png"))
                .forEach(r -> map.add("files", r));

        var result = restTemplate.postForObject(getImageUrl(), map, float[][].class);

        log.info("out vectorizeImage");

        return result;
    }
}
