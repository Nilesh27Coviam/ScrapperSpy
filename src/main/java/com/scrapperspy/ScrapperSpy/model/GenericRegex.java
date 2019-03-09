package com.scrapperspy.ScrapperSpy.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@Document(collection = GenericRegex.COLLECTION_NAME)
public class GenericRegex {
    public static final String COLLECTION_NAME = "GENERIC_REGEX";

    @Field(value = "CATEGORY")
    private String category;

    @Field(value = "ATTRIBUTE_NAME")
    private String attributeName;

    @Field(value = "REGEX")
    private List<Pattern> regex;


}
