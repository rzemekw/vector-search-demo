package com.ittouch.vectorsearchdemo.service;

import com.ittouch.vectorsearchdemo.repository.ImageRepository;
import com.ittouch.vectorsearchdemo.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Resource get(Long id) {
        log.info("in get for {}", id);

        var imageEntity = imageRepository.findById(id).orElseThrow();

        var resource = FileUtils.fromByteArray(imageEntity.getData(), imageEntity.getFilename());

        log.info("out get for {}", id);

        return resource;
    }
}
