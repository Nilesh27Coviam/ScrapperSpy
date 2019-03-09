package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.model.GenericRegex;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalysisExecutor implements Runnable{

    List<String> jsonValueList;
    ConcurrentHashMap detailedReportMap;
    List<GenericRegex> regexList;


    public AnalysisExecutor(List<String> jsonValueList, ConcurrentHashMap detailedReportMap, List<GenericRegex> regexList) {
        this.jsonValueList = jsonValueList;
        this.detailedReportMap = detailedReportMap;
        this.regexList = regexList;
    }

    @Override
    public void run(){


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
