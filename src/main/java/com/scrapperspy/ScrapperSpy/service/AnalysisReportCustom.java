package com.scrapperspy.ScrapperSpy.service;

import org.json.simple.JSONObject;

import java.util.List;

public interface AnalysisReportCustom {

    List<JSONObject> getAnalysiedData(String category,String source);
}
