package com.scrapperspy.ScrapperSpy.repository;

import com.scrapperspy.ScrapperSpy.model.CategorySourceCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySourceRepo extends MongoRepository<CategorySourceCollection,String>{

}