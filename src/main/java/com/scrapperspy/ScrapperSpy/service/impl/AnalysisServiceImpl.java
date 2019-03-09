package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.dao.AnalysisReportDAO;
import com.scrapperspy.ScrapperSpy.model.AnalysisReport;
import com.scrapperspy.ScrapperSpy.model.ExternalProduct;
import com.scrapperspy.ScrapperSpy.model.GenericRegex;
import com.scrapperspy.ScrapperSpy.repository.AnalysisReportRepo;
import com.scrapperspy.ScrapperSpy.repository.GenericRegexRepo;
import com.scrapperspy.ScrapperSpy.service.AnalysisService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AnalysisServiceImpl implements AnalysisService{

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    GenericRegexRepo genericRegexRepo;

    @Autowired
    AnalysisReportRepo  analysisReportRepo;

   public static Map<String, Integer> detailReport=new HashMap<>();
    @Override
    public AnalysisReportDAO generateReport(String category, String source) {

        String collectionName = "EXT_PRD_"+category+"_"+source;

        List<ExternalProduct> externalProductList = mongoTemplate.findAll(ExternalProduct.class, collectionName);
        List<GenericRegex> genericRegexList = genericRegexRepo.findAllByCategory(category);
        Map<String, Integer> detailReport=new HashMap<>();
        int productWithAllDefining = 0;
        for(ExternalProduct externalProduct : externalProductList){
            JSONObject jsonObject = externalProduct.getProductJson();
            Set<String> keys = jsonObject.keySet();
            Iterator value = keys.iterator();
            List<String> valuesOfJsonObject = new ArrayList<>();
            while(value.hasNext() ) {
                valuesOfJsonObject.add(jsonObject.get(value.next()).toString());
            }
            if(isAllDefiningAvailable(valuesOfJsonObject, genericRegexList,detailReport)){
                productWithAllDefining++;
            }
        }
        AnalysisReport analysisReport = AnalysisReport.builder()
                .category(category).source(source)
                .totalCrawlProduct(externalProductList.size())
                .countWithAllDefining(productWithAllDefining)
                .detaildReport(detailReport).build();
        analysisReport = analysisReportRepo.insert(analysisReport);

        AnalysisReportDAO analysisReportDAO = new AnalysisReportDAO();
        BeanUtils.copyProperties(analysisReport, analysisReportDAO);

        return analysisReportDAO;
    }

    public boolean isAllDefiningAvailable(List<String> values, List<GenericRegex> genericRegexList,Map<String,Integer> detailDataMap){
        boolean available = true;
        int foundAttrib = 0;
        for(GenericRegex genericRegex : genericRegexList){
            if(findValueInProductJson(genericRegex, values)){
                foundAttrib++;
                if(!detailDataMap.containsKey(genericRegex.getAttributeName())){
                    detailDataMap.put(genericRegex.getAttributeName(),1);
                }else{
                    int count=detailDataMap.get(genericRegex.getAttributeName())+1;
                    detailDataMap.put(genericRegex.getAttributeName(),count);
                }
            } else {
                available = false;
                break;
            }
        }
        if(foundAttrib == genericRegexList.size()){
            available = true;
        }
        return available;
    }

    public boolean findValueInProductJson(GenericRegex genericRegex, List<String> values){
        boolean found = false;
        for(String value : values){
            for(String regexString : genericRegex.getRegex()){
                Pattern regex = Pattern.compile(regexString);
                Matcher matcher = regex.matcher(value);
                if(matcher.find()){
                    found = true;
                    break;
                }
            }
            if(found){
                break;
            }
        }
        return found;
    }


    @Override public JSONObject getResult(String category, String source) {



        return null;
    }
}
