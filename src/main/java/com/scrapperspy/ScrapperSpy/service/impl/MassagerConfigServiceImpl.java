package com.scrapperspy.ScrapperSpy.service.impl;

import com.google.gson.JsonObject;
import com.scrapperspy.ScrapperSpy.FieldNames;
import com.scrapperspy.ScrapperSpy.model.ExternalProduct;
import com.scrapperspy.ScrapperSpy.model.GenericRegex;
import com.scrapperspy.ScrapperSpy.model.MassagerConfig;
import com.scrapperspy.ScrapperSpy.repository.MassagerConfigRepo;
import com.scrapperspy.ScrapperSpy.service.MassagerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service public class MassagerConfigServiceImpl implements MassagerService {

    @Autowired MongoTemplate mongoTemplate;

    @Autowired MassagerConfigRepo massagerConfigRepo;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override public void massageData(String category, String source) {

        String collectionName = "EXT_PRD_" + category + "_" + source;

        List<ExternalProduct> externalProductList =
            mongoTemplate.findAll(ExternalProduct.class, collectionName);

        List<MassagerConfig> massagerConfigs = massagerConfigRepo.findByCategory(category);

        for (ExternalProduct externalProduct : externalProductList) {
            CollectAttributeInformation(externalProduct, source, category, massagerConfigs);
        }
    }

    public void CollectAttributeInformation(ExternalProduct externalProduct, String source,
        String category, List<MassagerConfig> massagerConfigs) {
        {
            String massageCollectionName = category + "_" + source + "_" + "MASSAGED_DATA";
            try {
                // BaseExternalProductModel
                JSONObject jsonModel = new JSONObject();
                jsonModel.put(FieldNames.SOURCE, externalProduct.getSource());

                JSONObject externalProdctJson = externalProduct.getProductJson();
                for (MassagerConfig massagerConfig : massagerConfigs) {
                    try {
                        LOG.debug("Starting extraction for config {}",
                            massagerConfig.getAttributeName());

                            findAndSetAttributeValue(jsonModel, massagerConfig, externalProdctJson);
                    } catch (Exception e) {
                        LOG.error("Error while adding the data {} With exception {}",
                            externalProduct.getProductJson().get("PRODUCT_URL"), e);
                    }
                }
                mongoTemplate.insert(jsonModel, massageCollectionName);
            } catch (Exception e) {
                LOG.error(
                    "Exception in the process of massager for source {} category {} Exception {}",
                    source, category, e);
            }
        }
    }

    public void findAndSetAttributeValue(JSONObject model, MassagerConfig massagerConfig,
        JSONObject externalProduct) {
        {
            String attributeName = massagerConfig.getAttributeName();

            List<String> value = null;
            String extractedValue = null;
            switch (massagerConfig.getExtractionLogic()) {
                case "SIMPLE_REGEX_MATCH":
                    if (StringUtils.isEmpty((String) model.get(attributeName))) {
                        // value not set
                        value = extractAndTransform(externalProduct, massagerConfig.getLookupKey(),
                            massagerConfig.getLookupRegex(),
                            massagerConfig.getTransformationRegex(), false);
                        if (CollectionUtils.isNotEmpty(value)) {
                            extractedValue = value.get(0);
                        }
                    }
                    break;
                case "MULTI_VALUE":
                    value = extractAndTransformMultipleValues(externalProduct,
                        massagerConfig.getLookupKey(), massagerConfig.getLookupRegex(),
                        massagerConfig.getTransformationRegex(),
                        massagerConfig.getMultiValueSeparator());

                    if (CollectionUtils.isNotEmpty(value)) {
                        extractedValue = value.get(0);
                    }
                    break;
                case "EXTRA_INFO":
                    if (null == model.get(attributeName)) {
                        value = extractAndTransformFromExtraInfo(externalProduct,
                            massagerConfig.getLookupRegex(),
                            massagerConfig.getTransformationRegex());
                        if (CollectionUtils.isNotEmpty(value)) {
                            extractedValue = value.get(0);
                        }
                    }
            }

            if (MapUtils.isNotEmpty(massagerConfig.getReplacementMap())) {
                extractedValue =
                    replaceAll(extractedValue, massagerConfig.getReplacementMap(), false);
            }

            if (StringUtils.isNotEmpty(extractedValue)) {
                model.put(attributeName, extractedValue.trim());
            } else if (StringUtils.isNotEmpty(massagerConfig.getDefaultValue()) && StringUtils
                .isEmpty((String) model.get(attributeName))) {
                model.put(attributeName, massagerConfig.getDefaultValue());
            }
        }
    }

        public String replaceAll (String value, Map < String, String > replacementMap,
        boolean replaceRepeatedly){
            String replacedValue = value;
            if (StringUtils.isEmpty(value)) {
                return StringUtils.EMPTY;
            }
            if (replaceRepeatedly) {
                for (Map.Entry<String, String> replacement : replacementMap.entrySet()) {
                    while (replacedValue.contains(replacement.getKey())) {
                        replacedValue =
                            replacedValue.replaceAll(replacement.getKey(), replacement.getValue());
                    }
                }
            } else {
                for (Map.Entry<String, String> replacement : replacementMap.entrySet()) {
                    replacedValue =
                        replacedValue.replaceAll(replacement.getKey(), replacement.getValue());
                }
            }
            return replacedValue;
        }

        public List<String> extractAndTransformFromExtraInfo (JSONObject
        product, List < String > regexListToMatch, List < String > transformRegexList){
            List<String> transformedValues = new ArrayList<>();
            try {
                List<String> values = (List<String>) product.get(FieldNames.EXTRA_INFO);
                if (CollectionUtils.isNotEmpty(values)) {
                    outerloop:
                    for (String value : values) {
                        int i = 0;
                        Matcher matcher;
                        for (String regex : regexListToMatch) {
                            Pattern regexPattern=null;
                            String result = null;
                            regexPattern=Pattern.compile(regex);
                            matcher = regexPattern.matcher(value);
                            if (matcher.find()) {
                                result = matcher.group();
                                result =
                                    regexPattern.matcher(result).replaceFirst(transformRegexList.get(i))
                                        .trim();
                                transformedValues.add(result);
                                break outerloop;
                            }
                            i++;
                        }
                    }
                } else {
                    LOG.warn("no extra info found for {}", product.get(FieldNames.PRODUCT_NAME));
                }
            } catch (Exception e) {
                LOG.error("Error while extracting value {} for key {}",
                    product.get(FieldNames.EXTRA_INFO).toString(), FieldNames.EXTRA_INFO, e);
            }
            return transformedValues;
        }

        public List<String> extractAndTransform (JSONObject product, String
        key, List < String > regexListToMatch, List < String > transformRegexList,
        boolean isReplacementNeeded){
            List<String> transformedValues = new ArrayList<>();

            try {
                String value = (String) product.get(key);
                if (StringUtils.isEmpty(value)) {
                    return transformedValues;
                }
                transformedValues = new ArrayList<>();
                int i = 0;
                Matcher matcher;
                for (String regex : regexListToMatch) {
                    String result = null;
                    Pattern regexPattern;
                    regexPattern=Pattern.compile(regex);
                    matcher = regexPattern.matcher(value);
                    if (matcher.find()) {
                        if (isReplacementNeeded) {
                            result = matcher.group().trim();
                            transformedValues.add(result);
                        } else {
                            result = matcher.group();
                            result = regexPattern.matcher(result).replaceFirst(transformRegexList.get(i))
                                .trim();
                            transformedValues.add(result);
                            break;
                        }
                    }
                    i++;
                }
            } catch (Exception ex) {
                LOG.error("Error while extracting value {} for key {}", product.get(key).toString(),
                    key, ex);
            }
            return transformedValues;
        }

        public List<String> extractAndTransformMultipleValues (JSONObject product, String
        key, List < String > regexListToMatch, List < String > transformRegexList, String
        valueSeparator){
            List<String> transformedValues = new ArrayList<>();

            try {
                String value = (String) product.get(key);
                if (StringUtils.isEmpty(value)) {
                    return transformedValues;
                }
                transformedValues = new ArrayList<>();
                List<String> valueList = new ArrayList<>();
                int i = 0;
                Matcher matcher;
                for (String regex : regexListToMatch) {
                    Pattern regexPattern=null;
                    String result = null;
                    regexPattern=Pattern.compile(regex);
                    matcher = regexPattern.matcher(value);
                    while (matcher.find()) {
                        result = matcher.group();
                        result = regexPattern.matcher(result).replaceFirst(transformRegexList.get(i));
                        if (StringUtils.isNotEmpty(result)) {
                            result = result.trim();
                        }
                        valueList.add(result);
                    }
                    if (CollectionUtils.isNotEmpty(valueList)) {
                        transformedValues.add(String.join(valueSeparator, valueList));
                    }
                    i++;
                }
            } catch (Exception ex) {
                LOG.error("Error while extracting value {} for key {}", product.get(key).toString(),
                    key);
            }
            return transformedValues;
        }
    }

