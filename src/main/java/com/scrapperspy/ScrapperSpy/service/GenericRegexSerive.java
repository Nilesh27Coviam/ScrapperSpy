package com.scrapperspy.ScrapperSpy.service;

import java.util.List;
import com.scrapperspy.ScrapperSpy.dao.GenericRegexDao;
import com.scrapperspy.ScrapperSpy.model.GenericRegex;

public interface GenericRegexSerive {

    boolean insertAll(List<GenericRegexDao> genericRegexDaoList);
}
