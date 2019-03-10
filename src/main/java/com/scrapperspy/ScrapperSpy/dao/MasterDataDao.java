package com.scrapperspy.ScrapperSpy.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class MasterDataDao {

    @Field(value = "CATEGORY")
    private String category;

    @Field(value = "ATTRIBUTE_NAME")
    private String attributename;

    @Field(value = "VALUE")
    private long value;
}
