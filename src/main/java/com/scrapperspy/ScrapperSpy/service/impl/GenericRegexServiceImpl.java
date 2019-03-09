package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.dao.GenericRegexDao;
import com.scrapperspy.ScrapperSpy.model.GenericRegex;
import com.scrapperspy.ScrapperSpy.repository.GenericRegexRepo;
import com.scrapperspy.ScrapperSpy.service.GenericRegexSerive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenericRegexServiceImpl implements GenericRegexSerive{

    @Autowired
    GenericRegexRepo genericRegexRepo;
    @Override
    public boolean insert(GenericRegexDao genericRegexDao) {
        boolean isInserted = true;
        try{
            GenericRegex genericRegex = new GenericRegex();
            BeanUtils.copyProperties(genericRegex, genericRegexDao);
            genericRegexRepo.insert(genericRegex);
        } catch (Exception e){
            log.error("Error occure while adding {} in GENERIC_REGEX due to {}", genericRegexDao.toString(), e);
            isInserted = false;
        }

        return isInserted;
    }
}
