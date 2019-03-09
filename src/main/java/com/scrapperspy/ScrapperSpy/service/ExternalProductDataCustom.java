package com.scrapperspy.ScrapperSpy.service;

import com.scrapperspy.ScrapperSpy.model.ExternalProduct;

import java.util.List;

public interface ExternalProductDataCustom {

    List<ExternalProduct> getCtegoryData(String category);
}
