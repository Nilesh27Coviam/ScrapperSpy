package com.scrapperspy.ScrapperSpy.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
import java.util.Map;

@Setter
@Getter
@Document(collection = AnalysisReport.COLLECTION_NAME)
@CompoundIndex(name = "PRISTINE_CATEGORY_AND_SOURCE_AND_ORDER",
        def = "{'CATEGORY' : 1, " + "'SOURCE' : 1}", unique = true)
public class AnalysisReport {

    public static final String COLLECTION_NAME = "ANALYSIS_REPORT";

    @Field(value = "CATEGORY")
    private String category;

    @Field(value = "SOURCE")
    private String source;

    @Field(value = "TOTAL_CRAWL_PRODUCT")
    private long totalCrawlProduct;

    @Field(value = "COUNT_WITH_ALL_DEFINING")
    private long countWithAllDefining;

    @Field(value = "DETAILED_REPORT")
    private Map detaildReport;

    @Field(value = "PERCENTAGE")
    private float percentage;
}
