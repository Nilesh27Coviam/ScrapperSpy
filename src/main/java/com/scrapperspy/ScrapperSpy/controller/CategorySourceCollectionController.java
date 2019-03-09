package com.scrapperspy.ScrapperSpy.controller;

import com.scrapperspy.ScrapperSpy.dao.CategorySourceDAO;
import com.scrapperspy.ScrapperSpy.response.CategorySourceFilter;
import com.scrapperspy.ScrapperSpy.service.CategorySource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "/categorySource")
public class CategorySourceCollectionController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    CategorySource categorySource;

    @RequestMapping(value = "/getFilters", method = RequestMethod.GET)
    public CategorySourceDAO getCategoryAndSource(){
        Map<String,List<String>> filters = new HashMap<>();
        CategorySourceDAO categorySourceDAO = null;
        List<CategorySourceFilter> categorySourceFilters = categorySource.getCategorySource();
        if(CollectionUtils.isNotEmpty(categorySourceFilters)){
            filters = categorySourceFilters.stream().collect(
                    Collectors.toMap(filter -> filter.getCategory(),filter -> filter.getSourceList()));
            categorySourceDAO = new CategorySourceDAO().builder().filters(filters).build();
        }else{
            LOG.error("No filters found in category source collection");
        }
        return categorySourceDAO;
    }

}