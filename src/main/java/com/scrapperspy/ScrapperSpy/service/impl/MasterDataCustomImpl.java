package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.model.AnalysisReport;
import com.scrapperspy.ScrapperSpy.model.MasterData;
import com.scrapperspy.ScrapperSpy.repository.MasterDataRepo;
import com.scrapperspy.ScrapperSpy.service.MasterDataCustome;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MasterDataCustomImpl implements MasterDataCustome {

    @Autowired MasterDataRepo masterDataRepo;

    @Autowired MongoTemplate mongoTemplate;

    @Override public List<MasterData> getData(String category, String attributeName) {

        Query query = new Query();
        List<MasterData> results=new ArrayList<>();
        if (StringUtils.isNotBlank(category)  && StringUtils.isNotBlank(attributeName)) {
            query.addCriteria(Criteria.where("CATEGORY").is(category));
            query.addCriteria(Criteria.where("ATTRIBUTE_NAME").is(attributeName));
            results = mongoTemplate.find(query, MasterData.class);
        }else
        {
           return null;
        }

        return results;
    }
}
