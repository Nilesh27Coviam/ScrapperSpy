package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.FieldNames;
import com.scrapperspy.ScrapperSpy.model.ExternalProduct;
import com.scrapperspy.ScrapperSpy.service.ExternalProductDataCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ExternalProductDataCustomImpl implements ExternalProductDataCustom{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ExternalProduct> getCtegoryData(String category) {
        Query query=new Query(Criteria.where(FieldNames.CATEGORY).is(category.toString()));
        return mongoTemplate.find(query, ExternalProduct.class, ExternalProduct.COLLECTION_NAME);
    }
}
