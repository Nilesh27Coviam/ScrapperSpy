package com.scrapperspy.ScrapperSpy.model;

import com.scrapperspy.ScrapperSpy.FieldNames;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.json.simple.JSONObject;

import java.util.Date;

@Document(collection = ExternalProduct.COLLECTION_NAME)
public class ExternalProduct {

    public static final String COLLECTION_NAME = "EXT_PRODUCT_COLLECTION";

    @Field(value = FieldNames.PRODUCT_JSON)
    private JSONObject productJson;

    @Field(value = FieldNames.CRAWL_DATE)
    private Date crawlDate;

    @Field(value = FieldNames.CATEGORY)
    private String category;

    @Field(value = FieldNames.SOURCE)
    private String source;

    public JSONObject getProductJson() {
        return productJson;
    }

    public void setProductJson(JSONObject productJson) {
        this.productJson = productJson;
    }

    public Date getCrawlDate() {
        return crawlDate;
    }

    public void setCrawlDate(Date crawlDate) {
        this.crawlDate = crawlDate;
    }

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
}
