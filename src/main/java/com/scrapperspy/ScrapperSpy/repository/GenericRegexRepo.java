package com.scrapperspy.ScrapperSpy.repository;

import com.scrapperspy.ScrapperSpy.model.GenericRegex;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericRegexRepo extends MongoRepository<GenericRegex, String> {

    List<GenericRegex> findByCategory(String category);
}
