package com.scrapperspy.ScrapperSpy.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = MasterData.COLLECTION_NAME)
public class MasterData {
    public static final String COLLECTION_NAME = "MASTER_DATA";

    @Field(value = "CATEGORY")
    private String category;

    @Field(value = "ATTRIBUTE_NAME")
    private String attributename;

    @Field(value = "VALUE")
    private String value;
}
