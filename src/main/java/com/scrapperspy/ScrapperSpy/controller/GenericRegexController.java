package com.scrapperspy.ScrapperSpy.controller;

import java.util.List;
import com.scrapperspy.ScrapperSpy.dao.GenericRegexDao;
import com.scrapperspy.ScrapperSpy.service.GenericRegexSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/regex")
public class GenericRegexController {

    @Autowired
    GenericRegexSerive genericRegexSerive;

    @RequestMapping(value = "/insertAll", method = RequestMethod.POST)
    public boolean insertGenericRegex(@RequestBody List<GenericRegexDao> genericRegexDaoList){
        return genericRegexSerive.insertAll(genericRegexDaoList);
    }
}
