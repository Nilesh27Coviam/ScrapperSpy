package com.scrapperspy.ScrapperSpy.controller;

import com.scrapperspy.ScrapperSpy.dao.AnalysisReportDAO;
import com.scrapperspy.ScrapperSpy.service.AnalysisReportCustom;
import com.scrapperspy.ScrapperSpy.service.AnalysisService;
import com.scrapperspy.ScrapperSpy.service.DynamicCrawler;
import com.scrapperspy.ScrapperSpy.service.MassagerService;
import com.scrapperspy.ScrapperSpy.service.impl.DynamicCrawlerImpl;
import com.scrapperspy.ScrapperSpy.model.DynamicCrawlerConfig;
import com.scrapperspy.ScrapperSpy.repository.DynamicCrawlerConfigRepo;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final DynamicCrawlerConfigRepo userRepository;

    @Autowired
    DynamicCrawler dynamicCrawler;

    @Autowired
    AnalysisService analysisService;

    @Autowired
    MassagerService massagerService;

    @Autowired
    AnalysisReportCustom analysisReportCustom;


    public UserController(DynamicCrawlerConfigRepo userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public DynamicCrawlerConfig addNewUsers(@RequestBody DynamicCrawlerConfig user) {
        LOG.info("Saving user.");
        return userRepository.save(user);
    }

    @RequestMapping(value = "/startCrawl", method = RequestMethod.GET)
    public Boolean startCrawl(){
        Boolean result = dynamicCrawler.startCrawl();
        return result;
    }

    @RequestMapping(value = "/analyse/website/data", method = RequestMethod.GET)
    public AnalysisReportDAO getReport(@RequestParam String source,@RequestParam String category){
        return analysisService.generateReport(category,source);
    }

    @RequestMapping(value = "/massage/data", method = RequestMethod.GET)
    public void MassageData (@RequestParam String source,@RequestParam String category){
        massagerService.massageData(category,source);
    }

    @RequestMapping(value = "/getprocessedresult", method = RequestMethod.GET)

    public List<JSONObject> getResult(@RequestParam String source,@RequestParam String category){
        return analysisReportCustom.getAnalysiedData(category, source);
    }

}
