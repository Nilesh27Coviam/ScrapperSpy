package com.scrapperspy.ScrapperSpy.service;

import com.scrapperspy.ScrapperSpy.dao.AnalysisReportDAO;

public interface AnalysisService {

    public AnalysisReportDAO generateReport(String category, String source);
}
