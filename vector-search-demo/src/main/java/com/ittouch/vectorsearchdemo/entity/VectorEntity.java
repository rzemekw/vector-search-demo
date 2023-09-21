package com.ittouch.vectorsearchdemo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.nio.ByteBuffer;

@Data
@Entity
@Table(schema = "vector", name = "vector")
public class VectorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data")
    private byte[] byteData;

    public float[] getData() {
        if (byteData == null) {
            return null;
        }
        var byteBuffer = ByteBuffer.wrap(byteData);
        var floatBuffer = byteBuffer.asFloatBuffer();
        float[] floatArray = new float[byteData.length / 4];
        floatBuffer.get(floatArray);
        return floatArray;
    }

    public void setData(float[] data) {
        if (data == null) {
            byteData = null;
            return;
        }
        var byteBuffer = ByteBuffer.allocate(data.length * 4);
        var floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        byteData = byteBuffer.array();
    }

    public VectorEntity() {
    }

    public VectorEntity(float[] data) {
        setData(data);
    }
}
