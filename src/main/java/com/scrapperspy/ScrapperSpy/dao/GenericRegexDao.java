package com.scrapperspy.ScrapperSpy.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
public class GenericRegexDao {

    @Field(value = "CATEGORY")
    private String category;

    @Field(value = "ATTRIBUTE_NAME")
    private String attributeName;

    @Field(value = "REGEX")
    private List<String> regex;
}
