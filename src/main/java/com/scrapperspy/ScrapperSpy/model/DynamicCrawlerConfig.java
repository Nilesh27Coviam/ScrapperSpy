package com.scrapperspy.ScrapperSpy.model;

import com.scrapperspy.ScrapperSpy.FieldNames;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document(collection = DynamicCrawlerConfig.COLLECTION_NAME)
public class DynamicCrawlerConfig {

    public static final String COLLECTION_NAME = "DYNAMIC_CRAWLER_CONFIG";

    @Field(FieldNames.CATEGORY)
    private String category;

    @Field(FieldNames.SOURCE)
    private String source;

    @Field(FieldNames.SEED_URL)
    private List<String> seedUrl;

    @Field(FieldNames.WAIT_TIME)
    private Integer waitTime;

    @Field(FieldNames.PRODUCT_URL_SELECTOR)
    private String productUrlSelector;

    @Field(FieldNames.NEXT_PAGE_SELECTOR)
    private String nextPageSelector;

    @Field(FieldNames.IS_ENABLED)
    private Boolean isEnabled;

    @Field(FieldNames.SOURCE_SEARCH_SELECTOR)
    private String sourceSearchSelector;

    @Field(FieldNames.CHROME_OPTIONS)
    private List<String> chromeOptions;

    @Field(FieldNames.SEED_URL_APPENDER)
    private String seedUrlAppender;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(List<String> seedUrl) {
        this.seedUrl = seedUrl;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public String getProductUrlSelector() {
        return productUrlSelector;
    }

    public void setProductUrlSelector(String productUrlSelector) {
        this.productUrlSelector = productUrlSelector;
    }

    public String getNextPageSelector() {
        return nextPageSelector;
    }

    public void setNextPageSelector(String nextPageSelector) {
        this.nextPageSelector = nextPageSelector;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getSourceSearchSelector() {
        return sourceSearchSelector;
    }

    public void setSourceSearchSelector(String sourceSearchSelector) {
        this.sourceSearchSelector = sourceSearchSelector;
    }

    public List<String> getChromeOptions() {
        return chromeOptions;
    }

    public void setChromeOptions(List<String> chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public String getSeedUrlAppender() {
        return seedUrlAppender;
    }

    public void setSeedUrlAppender(String seedUrlAppender) {
        this.seedUrlAppender = seedUrlAppender;
    }
}

