package com.scrapperspy.ScrapperSpy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicCrawlerConfig extends MongoRepository<DynamicCrawlerConfig,String>{
}
