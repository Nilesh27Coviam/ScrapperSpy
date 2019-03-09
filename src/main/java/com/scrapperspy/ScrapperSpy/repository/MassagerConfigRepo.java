package com.scrapperspy.ScrapperSpy.repository;

import com.scrapperspy.ScrapperSpy.model.GenericRegex;
import com.scrapperspy.ScrapperSpy.model.MassagerConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MassagerConfigRepo extends MongoRepository<MassagerConfig,String> {

    List<MassagerConfig> findByCategory(String category);
}
