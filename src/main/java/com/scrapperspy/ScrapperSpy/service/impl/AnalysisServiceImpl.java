package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.dao.AnalysisReportDAO;
import com.scrapperspy.ScrapperSpy.model.AnalysisReport;
import com.scrapperspy.ScrapperSpy.model.ExternalProduct;
import com.scrapperspy.ScrapperSpy.model.GenericRegex;
import com.scrapperspy.ScrapperSpy.repository.AnalysisReportRepo;
import com.scrapperspy.ScrapperSpy.repository.GenericRegexRepo;
import com.scrapperspy.ScrapperSpy.service.AnalysisService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
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

        Update update1 = new Update();
        update1.set("REGEX", Arrays.asList("(?i)((\\d+(\\.\\d+)?)\\s*(\\\"|\\-)?\\s*(Inches|inch|Inci|inc)?|(\\d+(\\.\\d+)?)\\s*(fhd|qhd|qled|led|hd)|(\\d+(\\.\\d+)?)\"|^(\\d+(\\.\\d+)?)$)"));
        mongoTemplate.updateFirst(new Query(Criteria.where("ATTRIBUTE_NAME").is("SCREEN_SIZE").and("CATEGORY").is("COMPUTER")),update1,"GENERIC_REGEX");

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
        AnalysisReport analysisReport;
        Boolean isUpdate = false;
        if(analysisReportRepo.findByCategoryAndSource(category, source).size() == 0){
            analysisReport = new AnalysisReport();
        } else {
            analysisReport = analysisReportRepo.findByCategoryAndSource(category, source).get(0);
            isUpdate = true;
        }

        analysisReport.setCategory(category);
        analysisReport.setSource(source);
        analysisReport.setTotalCrawlProduct(externalProductList.size());
        analysisReport.setCountWithAllDefining(productWithAllDefining);
        analysisReport.setDetaildReport(detailReport);

        float totalProductCount = analysisReport.getTotalCrawlProduct();
        float definingAttributeCount = analysisReport.getCountWithAllDefining();
        analysisReport.setPercentage((definingAttributeCount/totalProductCount)*100);
        if(isUpdate){
            Query query = new Query(Criteria.where("CATEGORY").is(category).and("SOURCE").is(source));
            Update update = new Update();
            update.set("TOTAL_CRAWL_PRODUCT", analysisReport.getTotalCrawlProduct());
            update.set("COUNT_WITH_ALL_DEFINING", analysisReport.getCountWithAllDefining());
            update.set("DETAILED_REPORT", analysisReport.getDetaildReport());
            update.set("PERCENTAGE", analysisReport.getPercentage());
            mongoTemplate.updateFirst(query, update, AnalysisReport.COLLECTION_NAME);
        } else {
            analysisReportRepo.save(analysisReport);
        }

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

}
