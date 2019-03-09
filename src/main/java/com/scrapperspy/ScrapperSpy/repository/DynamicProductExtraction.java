package com.scrapperspy.ScrapperSpy.repository;

import com.scrapperspy.ScrapperSpy.model.DynamicCrawlerProductExtractionConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicProductExtraction extends MongoRepository<DynamicCrawlerProductExtractionConfig,String> {

    DynamicCrawlerProductExtractionConfig findBySourceAndCategory(String source,String category);

}
