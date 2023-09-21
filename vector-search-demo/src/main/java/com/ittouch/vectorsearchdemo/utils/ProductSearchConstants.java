package com.ittouch.vectorsearchdemo.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductSearchConstants {
    public final static String INDEX_NAME = "products";

    public final static String ID_FIELD = "_id";

    public final static String IMAGE_VECTOR_FIELD = "imageVector";
    public final static String DESCRIPTION_VECTOR_FIELD = "descriptionVector";
    public final static String DESCRIPTION_FIELD = "description";
}
