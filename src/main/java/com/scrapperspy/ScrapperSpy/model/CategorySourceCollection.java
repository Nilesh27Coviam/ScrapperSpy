package com.scrapperspy.ScrapperSpy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document(collection = CategorySourceCollection.COLLECTION_NAME)
public class CategorySourceCollection {

    public static final String COLLECTION_NAME = "CATEGORY_SOURCE_COLLECTION";

    @Field(value = "CATEGORY")
    private String category;

    @Field(value = "SOURCE")
    private List<String> sourceList;
}