package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.FieldNames;
import com.scrapperspy.ScrapperSpy.model.CategorySourceCollection;
import com.scrapperspy.ScrapperSpy.response.CategorySourceFilter;
import com.scrapperspy.ScrapperSpy.service.CategorySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.scrapperspy.ScrapperSpy.FieldNames.SOURCE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Service
public class CategorySourceImpl implements CategorySource{

    @Autowired
    MongoTemplate mongoTemplate;

    private String SOURCE_LIST = "sourceList";
    private String CATEGORY = "category";

    @Override
    public List<CategorySourceFilter> getCategorySource() {

        Aggregation aggregation = Aggregation.
                newAggregation(Aggregation.group(FieldNames.CATEGORY).addToSet(SOURCE).as(SOURCE_LIST),
                        project(SOURCE_LIST).and(CATEGORY).previousOperation());
        AggregationResults<CategorySourceFilter> aggregationResults = mongoTemplate.
                aggregate(aggregation, CategorySourceCollection.COLLECTION_NAME,CategorySourceFilter.class);
        return aggregationResults.getMappedResults();
    }
}