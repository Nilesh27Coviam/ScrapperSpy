package com.scrapperspy.ScrapperSpy.dao;

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
    private long longCountWithAllDefining;
}
