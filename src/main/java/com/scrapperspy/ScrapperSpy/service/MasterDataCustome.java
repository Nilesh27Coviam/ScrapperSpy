package com.scrapperspy.ScrapperSpy.service;

import com.scrapperspy.ScrapperSpy.model.MasterData;

import java.util.List;

public interface MasterDataCustome {


    List<MasterData> getData (String category,String attributeName);
}
