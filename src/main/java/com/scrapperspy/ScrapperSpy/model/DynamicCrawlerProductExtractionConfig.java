package com.scrapperspy.ScrapperSpy.model;

import com.scrapperspy.ScrapperSpy.FieldNames;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = DynamicCrawlerProductExtractionConfig.COLLECTION_NAME)
public class DynamicCrawlerProductExtractionConfig {

    public static final String COLLECTION_NAME = "DYNAMIC_CRAWLER_PRODUCT_EXTRACTION_CONFIG";

    @Field(FieldNames.CATEGORY)
    private String category;

    @Field(FieldNames.SOURCE)
    private String source;

    @Field(FieldNames.PRODUCT_NAME_SELECOR)
    private String productNameSelector;

    @Field(FieldNames.PRODUCT_INFO_KEYS_SELECTOR)
    private List<String> productInfoKeysSelector;

    @Field(FieldNames.PRODUCT_INFO_VALUES_SELECTOR)
    private List<String> productInfoValuesSelector;

    @Field(FieldNames.SPECIFICATION_KEYS_SELECTOR)
    private List<String> specificationKeysSelector;

    @Field(FieldNames.SPECIFICATION_VALUES_SELECTOR)
    private List<String> specificationValuesSelector;

    @Field(FieldNames.OTHER_DETAILS_SELECTOR)
    private List<String> otherDetailsSelector;

    @Field(FieldNames.VARIANTS_SELECTOR)
    private List<String> variantsSelector;

    @Field(FieldNames.WAIT_TIME)
    private int waitTime;

    @Field(FieldNames.IS_NEED_TO_SCROLL_DOWN)
    private boolean isNeedToScrollDown;

    @Field(FieldNames.SCROLL_RETRY_COUNT)
    private int scrollRetryCount;

    @Field(FieldNames.SCROLL_DOWN_XPATH)
    private String scrollDownXpath;

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

    public String getProductNameSelector() {
        return productNameSelector;
    }

    public void setProductNameSelector(String productNameSelector) {
        this.productNameSelector = productNameSelector;
    }

    public List<String> getProductInfoKeysSelector() {
        return productInfoKeysSelector;
    }

    public void setProductInfoKeysSelector(List<String> productInfoKeysSelector) {
        this.productInfoKeysSelector = productInfoKeysSelector;
    }

    public List<String> getProductInfoValuesSelector() {
        return productInfoValuesSelector;
    }

    public void setProductInfoValuesSelector(List<String> productInfoValuesSelector) {
        this.productInfoValuesSelector = productInfoValuesSelector;
    }

    public List<String> getSpecificationKeysSelector() {
        return specificationKeysSelector;
    }

    public void setSpecificationKeysSelector(List<String> specificationKeysSelector) {
        this.specificationKeysSelector = specificationKeysSelector;
    }

    public List<String> getSpecificationValuesSelector() {
        return specificationValuesSelector;
    }

    public void setSpecificationValuesSelector(List<String> specificationValuesSelector) {
        this.specificationValuesSelector = specificationValuesSelector;
    }

    public List<String> getOtherDetailsSelector() {
        return otherDetailsSelector;
    }

    public void setOtherDetailsSelector(List<String> otherDetailsSelector) {
        this.otherDetailsSelector = otherDetailsSelector;
    }

    public List<String> getVariantsSelector() {
        return variantsSelector;
    }

    public void setVariantsSelector(List<String> variantsSelector) {
        this.variantsSelector = variantsSelector;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isNeedToScrollDown() {
        return isNeedToScrollDown;
    }

    public void setNeedToScrollDown(boolean needToScrollDown) {
        isNeedToScrollDown = needToScrollDown;
    }

    public int getScrollRetryCount() {
        return scrollRetryCount;
    }

    public void setScrollRetryCount(int scrollRetryCount) {
        this.scrollRetryCount = scrollRetryCount;
    }

    public String getScrollDownXpath() {
        return scrollDownXpath;
    }

    public void setScrollDownXpath(String scrollDownXpath) {
        this.scrollDownXpath = scrollDownXpath;
    }
}
