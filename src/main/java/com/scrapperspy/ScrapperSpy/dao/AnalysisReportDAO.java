package com.scrapperspy.ScrapperSpy.dao;

import java.util.Map;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class AnalysisReportDAO {

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
