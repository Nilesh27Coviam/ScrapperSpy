package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.model.AnalysisReport;
import com.scrapperspy.ScrapperSpy.model.ExternalProduct;
import com.scrapperspy.ScrapperSpy.repository.AnalysisReportRepo;
import com.scrapperspy.ScrapperSpy.service.AnalysisReportCustom;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AnalysisReportCustomImpl implements AnalysisReportCustom {
    @Autowired AnalysisReportRepo analysisReportRepo;

    @Autowired MongoTemplate mongoTemplate;
    @Override
    public List<JSONObject> getAnalysiedData(String category, String source) {

        List<JSONObject> finalResult=new ArrayList<>();
        Query query = new Query();
        List<AnalysisReport> results=new ArrayList<>();
        if (StringUtils.isNotBlank(category)  && StringUtils.isNotBlank(source)) {
            query.addCriteria(Criteria.where("CATEGORY").is(category));
            query.addCriteria(Criteria.where("SOURCE").is(source));
            results =
                mongoTemplate.find(query,AnalysisReport.class);
        }else
        {
            results=mongoTemplate.findAll(AnalysisReport.class,AnalysisReport.COLLECTION_NAME);
        }
        if (results != null && results.size() > 0) {
            for (AnalysisReport analysisReport : results) {
                JSONObject responceObj = new JSONObject();
                if (analysisReport.getDetaildReport() != null
                    && analysisReport.getDetaildReport().size() > 0) {

                    responceObj.put("SOURCE", analysisReport.getSource());
                    responceObj.put("CATEGORY", analysisReport.getCategory());
                    List<JSONObject> data = new ArrayList<>();
                    Set<String> keys = analysisReport.getDetaildReport().keySet();
                    for (String key : keys) {
                        JSONObject innnerObj = new JSONObject();
                        innnerObj.put("key",key);
                        innnerObj.put("value", analysisReport.getDetaildReport().get(key));
                        data.add(innnerObj);
                    }
                    responceObj.put("DATA", data);
                }
                finalResult.add(responceObj);
            }
        }
        return finalResult;
    }
}
