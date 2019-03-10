package com.scrapperspy.ScrapperSpy.repository;

import com.scrapperspy.ScrapperSpy.model.MasterData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterDataRepo extends MongoRepository<MasterData, String>{
}
