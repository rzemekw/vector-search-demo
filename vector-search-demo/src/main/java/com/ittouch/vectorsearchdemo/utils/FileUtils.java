package com.ittouch.vectorsearchdemo.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;

@UtilityClass
public class FileUtils {
    public ByteArrayResource fromByteArray(byte[] content, String filename) {
        return new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}
