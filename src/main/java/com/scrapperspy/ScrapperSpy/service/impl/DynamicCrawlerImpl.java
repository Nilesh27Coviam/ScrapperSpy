package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.FieldNames;
import com.scrapperspy.ScrapperSpy.model.DynamicCrawlerConfig;
import com.scrapperspy.ScrapperSpy.model.DynamicCrawlerProductExtractionConfig;
import com.scrapperspy.ScrapperSpy.model.ExternalProduct;
import com.scrapperspy.ScrapperSpy.repository.DynamicCrawlerConfigRepo;
import com.scrapperspy.ScrapperSpy.repository.DynamicProductExtraction;
import com.scrapperspy.ScrapperSpy.repository.ExternalProductData;
import com.scrapperspy.ScrapperSpy.service.DynamicCrawler;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.rest.core.util.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.*;

@Service
public class DynamicCrawlerImpl implements DynamicCrawler{

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static List<String> productUrls = new ArrayList<>();

    @Autowired
    DynamicCrawlerConfigRepo configRepo;

    @Autowired
    DynamicProductExtraction extractionConfig;

    @Autowired
    ExternalProductData externalProductData;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Boolean startCrawl() {

        List<DynamicCrawlerConfig> dynamicCrawlerConfigs =
                configRepo.findByIsEnabled(Boolean.TRUE);
        if (CollectionUtils.isEmpty(dynamicCrawlerConfigs)) {
            LOG.error("No dynamic crawler config found");
        } else {
            for (DynamicCrawlerConfig dynamicCrawlerConfig : dynamicCrawlerConfigs) {
                WebDriver webDriver = null;
                try {
                    webDriver = callWebDriver();
                    LOG.warn("Borrowed chromedriver : {}", webDriver);
                    List<String> seedUrls = dynamicCrawlerConfig.getSeedUrl();
                    for (int i = 0; i < seedUrls.size(); i++) {
                        webDriver.get(seedUrls.get(i));
                        Thread.sleep(10000);
                        boolean isLastPage = Boolean.FALSE;
                        while (!isLastPage) {
                            LOG.warn("Current page url is : {}, with source : {}, category : {}",
                                    webDriver.getCurrentUrl(), dynamicCrawlerConfig.getSource(), dynamicCrawlerConfig.getCategory());
                            Thread.sleep(5000);
                            productUrls.addAll(processProductElements(dynamicCrawlerConfig.getProductUrlSelector(), webDriver));
                            if (productUrls.size()>0) {
                                if (!moveToNextPage(dynamicCrawlerConfig.getNextPageSelector(), webDriver)) {
                                    LOG.warn("Reached at last page -> url false : {}", webDriver.getCurrentUrl());
                                    isLastPage = Boolean.TRUE;

                                }
                            } else {
                                isLastPage = Boolean.TRUE;
                                LOG.warn("Reached at last page - > url : {}, source : {}, category : {}",
                                        webDriver.getCurrentUrl(), dynamicCrawlerConfig.getSource(), dynamicCrawlerConfig.getCategory());
                            }
                        }
                        DynamicCrawlerProductExtractionConfig productExtractionConfig = extractionConfig.
                                findBySourceAndCategory(dynamicCrawlerConfig.getSource(),dynamicCrawlerConfig.getCategory());
                            LOG.warn("{} list size and productExtractionConfig {}",productUrls.size(),productExtractionConfig);
                        for(String productUrl : productUrls){
                            try{
                                webDriver = callWebDriver();
                                webDriver.get(productUrl);
                                Thread.sleep(10000);
                                List<ExternalProduct> exractedProducts =
                                        extractProducts(productExtractionConfig,  webDriver);
                                LOG.warn("Number of records extracted {}",exractedProducts.size());
                                for(ExternalProduct externalProduct: exractedProducts){
                                    try{
                                        externalProduct.setCategory(productExtractionConfig.getCategory());
                                        externalProduct.setSource(productExtractionConfig.getSource());
                                        LOG.warn("the records to be inserted {}",externalProduct);
                                        //
                                        //
                                        //
                                        // externalProductData.save(externalProduct);
                                        String collectionName = "EXT_PRD_"+productExtractionConfig.getCategory()+"_"+productExtractionConfig.getSource();
                                        mongoTemplate.insert(externalProduct, collectionName);
                                    } catch (Exception e) {
                                        LOG.error("{} error occured due to {}", productUrl, e);
                                    }
                                }
                            } catch ( Exception e){
                                LOG.error("{} error occured due to {} outter try", productUrl, e);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Chrome driver",e);
                }
            }

        }
        return true;

    }

    public static WebDriver callWebDriver(){
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        List<String> options = Arrays.asList("--headless",
                "----disable-gpu",
                "--ignore-certificate-errors",
                "window-size=1920,1080");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(options);
        return new ChromeDriver(chromeOptions);
    }

    boolean moveToNextPage(String nextPageSelector, WebDriver webDriver) {
        boolean result = Boolean.FALSE;
        try {
            WebElement nextPageElement = webDriver.findElement(By.xpath(nextPageSelector));
            String productUrl = nextPageElement.getAttribute("href");
            if ("javascript:void(0)".equalsIgnoreCase(productUrl)) {
                return Boolean.FALSE;
            } else {
                int count = 4;
                while (count >= 0) {
                    String previousPageUrl = webDriver.getCurrentUrl();
                    Actions action = new Actions(webDriver);
                    action.moveToElement(nextPageElement).click().perform();
                    String nextPageUrl = webDriver.getCurrentUrl();
                    if (!previousPageUrl.equalsIgnoreCase(nextPageUrl)) {
                        LOG.warn("Moved to next page");
                        result = Boolean.TRUE;
                        break;
                    } else{
                        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();",nextPageElement);
                        nextPageUrl = webDriver.getCurrentUrl();
                        if (!previousPageUrl.equalsIgnoreCase(nextPageUrl)) {
                            LOG.warn("Moved to next page");
                            result = Boolean.TRUE;
                            break;
                        }
                    }
                    count--;
                }
            }
        }
        catch (Exception e) {
            result = Boolean.FALSE;
            LOG.warn("Error occurred, no next page found : {}, e");
        }
        return result;
    }


    List<String> processProductElements(String productSelector, WebDriver webDriver) {
        List<WebElement> productWebElements = webDriver.findElements(By.xpath(productSelector));
        LOG.warn("in  productWebElements {}",productWebElements.size());
        List<String> productUrls = new ArrayList<String>();
        for (WebElement productUrl : productWebElements) {
            LOG.warn("product url {}",productUrl);
            productUrls.add(productUrl.getAttribute("href"));
        }
        return productUrls;
    }

    List<ExternalProduct>








    extractProducts(DynamicCrawlerProductExtractionConfig extractionConfig, WebDriver webDriver) throws Exception {
        LOG.warn("Extracted events started");
        List<ExternalProduct> externalProducts = new ArrayList<>();
        List<String> variantSelectors = extractionConfig.getVariantsSelector();
        List<List<WebElement>> variantElements = new ArrayList<>();
        if (!CollectionUtils.isEmpty(variantSelectors)) {
            for (String variantSelector : variantSelectors) {
                List<WebElement> webElements = getElementsBySelector(variantSelector, webDriver);
                if (!CollectionUtils.isEmpty(webElements)) {
                    variantElements.add(webElements);
                }
            }
        }
        if (!CollectionUtils.isEmpty(variantElements)) {
            int depth = variantElements.size();
            int index = 0;
            processVariants(variantElements, depth, index, externalProducts, extractionConfig,
                    webDriver, variantSelectors);
        } else {
            ExternalProduct externalProduct =
                    getExtractedProduct(extractionConfig, webDriver);
            LOG.warn("Product get started");
            externalProducts.add(externalProduct);
        } LOG.warn("Extracted products : {} with source: {}, category : {}", externalProducts,
                extractionConfig.getSource(), extractionConfig.getCategory());
        return externalProducts;
    }

    List<WebElement> getElementsBySelector(String selector, WebDriver webDriver) {
        List<WebElement> webElements = new ArrayList<>();
        try {
            Thread.sleep(200);
            webElements = webDriver.findElements(By.xpath(selector));
        } catch (Exception e) {
            LOG.error("Not able to find any element with selector {}", selector, e);
        }
        return webElements;
    }

    void processVariants(List<List<WebElement>> variantElements, int currentDepth, int index,
                         List<ExternalProduct> result, DynamicCrawlerProductExtractionConfig extractionConfig, WebDriver webDriver, List<String> variantSelectors) throws Exception {
        try {
            if (currentDepth != 0) {
                List<WebElement> webElements = variantElements.get(index);
                // update web elements
                webElements = webDriver.findElements(By.xpath(variantSelectors.get(index)));
                for (int j = 0; j < webElements.size(); j++) {
                    // getting updated web elements
                    webElements = webDriver.findElements(By.xpath(variantSelectors.get(index)));
                    // move to variant url
                    Actions action = new Actions(webDriver);
                    action.moveToElement(webElements.get(j)).click().perform();
                    Thread.sleep(extractionConfig.getWaitTime());
                    LOG.warn("Clicked on variant attribute, current url is : {}", webDriver.getCurrentUrl());
                    processVariants(variantElements, currentDepth - 1, index + 1, result, extractionConfig,
                             webDriver, variantSelectors);
                    //product extraction
                    if (currentDepth == 1) {
                        ExternalProduct externalProduct =
                                getExtractedProduct(extractionConfig, webDriver);
                        result.add(externalProduct);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error occurred while processing variants with depth : {}, url : {}", currentDepth,
                    webDriver.getCurrentUrl(), e);
        }
    }

    ExternalProduct getExtractedProduct(DynamicCrawlerProductExtractionConfig extractionConfig,
                                        WebDriver webDriver) {
        ExternalProduct externalProduct = new ExternalProduct();
        LOG.warn("Product extraction started for url : {}", webDriver.getCurrentUrl(),
                extractionConfig.getCategory(), extractionConfig.getSource());
        JSONObject productJson = new JSONObject();
        productJson.put(FieldNames.PRODUCT_URL, webDriver.getCurrentUrl());
        String productName =
                webDriver.findElement(By.xpath(extractionConfig.getProductNameSelector())).getText();
        if (StringUtils.isNotBlank(productName)) {
            productJson.put(FieldNames.PRODUCT_NAME, productName);
        }

        try{
            if(extractionConfig.isNeedToScrollDown()) {
                int retryCount = extractionConfig.getScrollRetryCount();
                for (int i = 0; i < retryCount; i++) {
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    WebElement Element =
                            webDriver.findElement(By.xpath(extractionConfig.getScrollDownXpath()));
                    js.executeScript("arguments[0].scrollIntoView();", Element);
                    Thread.sleep(10000);
                    if (webDriver
                            .findElements(By.xpath(extractionConfig.getSpecificationKeysSelector().get(0)))
                            .size() > 0) {
                        LOG.warn("Scrolling sucessful for {}",productJson.get(FieldNames.PRODUCT_URL));
                        break;
                    }
                }
            }
        }catch (Exception e){
            LOG.error("Error while scrowling down {} product page URL {}",e,productJson.get(FieldNames.PRODUCT_URL));
        }
        //extracting attribute from list of specification selectors

        for (int i = 0; i < extractionConfig.getSpecificationKeysSelector().size(); i++) {
            String specKeyselector = extractionConfig.getSpecificationKeysSelector().get(i);
            String specValueSelector = extractionConfig.getSpecificationValuesSelector().get(i);
            if (StringUtils.isNotBlank(specKeyselector) && StringUtils.isNotBlank(specValueSelector)) {
                extractAttributeWithKeyValue(specKeyselector, specValueSelector, productJson, webDriver);
            }
        }

        //extracting attribute from product variant-attributes part
        for (int i = 0; i < extractionConfig.getProductInfoKeysSelector().size(); i++) {
            String productInfoKeySelector = extractionConfig.getProductInfoKeysSelector().get(i);
            String productInfoValueSelector = extractionConfig.getProductInfoValuesSelector().get(i);
            if (StringUtils.isNotBlank(productInfoKeySelector) && StringUtils.isNotBlank(productInfoValueSelector)) {
                    extractAttributeWithKeyValue(productInfoKeySelector, productInfoValueSelector,
                            productJson, webDriver);
            }
        }
        //extracting extra info
        List<String> extraInfoSelectors = extractionConfig.getOtherDetailsSelector();
        List<String> extraInfo = new ArrayList<>();
        if (!CollectionUtils.isEmpty(extraInfoSelectors)) {
            for (String extraInfoSelector : extraInfoSelectors) {
                List<WebElement> extraInfoWebElements = webDriver.findElements(By.xpath(extraInfoSelector));
                for (WebElement extraInfoWebElement : extraInfoWebElements) {
                    String extraInfoValue = extraInfoWebElement.getText();
                    if (StringUtils.isNotBlank(extraInfoValue)) {
                        extraInfo.add(extraInfoValue);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(extraInfo)) {
            productJson.put(FieldNames.EXTRA_INFO, extraInfo);
        }
        externalProduct.setProductJson(productJson);
        LOG.warn("Product extraction Completed for url : {}", webDriver.getCurrentUrl(),
                extractionConfig.getCategory(), extractionConfig.getSource());
        return externalProduct;
    }


    void extractAttributeWithKeyValue(String keySelector, String valueSelector,
                                      JSONObject productJson, WebDriver webDriver) {
        List<WebElement> keyElements = getElementsBySelector(keySelector, webDriver);
        List<WebElement> valueElements = getElementsBySelector(valueSelector, webDriver);
        for (int j = 0; j < keyElements.size() && j < valueElements.size(); j++) {
            String specificationKey = keyElements.get(j).getAttribute("innerText");
            String specificationValue = valueElements.get(j).getAttribute("innerText");
            if (StringUtils.isNotBlank(specificationKey) && StringUtils.isNotBlank(specificationValue)) {
                specificationKey = specificationKey.replaceAll("\\.", "-");
                productJson.put(specificationKey, specificationValue);
            }
        }
    }


}
