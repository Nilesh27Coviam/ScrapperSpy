package com.scrapperspy.ScrapperSpy.service;

import com.scrapperspy.ScrapperSpy.dao.AnalysisReportDAO;
import org.json.simple.JSONObject;

public interface AnalysisService {

    public AnalysisReportDAO generateReport(String category, String source);

}
