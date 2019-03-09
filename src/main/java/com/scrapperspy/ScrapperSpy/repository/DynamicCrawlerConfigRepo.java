package com.scrapperspy.ScrapperSpy.repository;

import com.scrapperspy.ScrapperSpy.model.DynamicCrawlerConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicCrawlerConfigRepo extends MongoRepository<DynamicCrawlerConfig,String> {

    List<DynamicCrawlerConfig> findByIsEnabled(Boolean temp);

}
