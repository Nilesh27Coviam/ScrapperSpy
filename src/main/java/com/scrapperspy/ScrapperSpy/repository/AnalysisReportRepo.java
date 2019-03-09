package com.scrapperspy.ScrapperSpy.repository;

import com.scrapperspy.ScrapperSpy.model.AnalysisReport;
import com.scrapperspy.ScrapperSpy.service.AnalysisReportCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisReportRepo extends MongoRepository<AnalysisReport, String>{
}
