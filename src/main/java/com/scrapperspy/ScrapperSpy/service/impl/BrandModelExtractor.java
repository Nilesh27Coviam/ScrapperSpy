package com.scrapperspy.ScrapperSpy.service.impl;

import com.scrapperspy.ScrapperSpy.FieldNames;
import com.scrapperspy.ScrapperSpy.model.MassagerConfig;
import com.scrapperspy.ScrapperSpy.model.MasterData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class BrandModelExtractor {
        @Autowired
        private MasterDataCustomImpl masterDataCustom;

        public JSONObject findAttributeValue(JSONObject externalProduct,
            MassagerConfig externalProductDataMassagerConfig, JSONObject model) {

            List<String> brandlist=new ArrayList<String>();
           List<MasterData> brandData=masterDataCustom.getData("COMPUTER","BRAND");
           for(MasterData attribute:brandData){
               if(!brandlist.contains(attribute.getValue())){
                   brandlist.add(attribute.getValue());
               }
           }

            if (brandlist != null && brandlist.size()>0 && StringUtils
                .isNotBlank(externalProductDataMassagerConfig.getLookupKey())
                && externalProduct.get(externalProductDataMassagerConfig.getLookupKey()) != null) {
                String[] productNameSplit = {};
                String productName =
                    externalProduct.get(externalProductDataMassagerConfig.getLookupKey()).toString();

                productName=replaceAll(productName,externalProductDataMassagerConfig.getReplacementMap(),false);

                if (brandlist != null) {
                    for (String attributeValue : brandlist) {
                        String brand = "(^|\\s+|\\n)(?i)("+attributeValue+")(\\s+|$)";
                        if (Pattern.compile(brand).matcher(productName).find()) {
                            model.put("BRAND", attributeValue);
                                productNameSplit = productName.split("(?i)" + attributeValue + "\\s");
                            break;
                        }
                    }

                    if (productNameSplit.length >= 2) {
                        String modelName = productNameSplit[1];
                        if(model.get("COLOR") != null) {
                            modelName = modelName
                                .replaceAll("(?i)\\s" + model.get("COLOR").toString() + "\\s", StringUtils.EMPTY)
                                .trim();
                        }
                        List<MasterData> ColorData=masterDataCustom.getData("COMPUTER","COLOR");
                        for (MasterData color : ColorData) {
                            modelName = modelName.replaceAll("(?i)\\s" + color.getValue() + "\\s", StringUtils.EMPTY).trim();
                        }
                       if(StringUtils.isBlank((String)model.get(FieldNames.MODEL)))
                            model.put("MODEL", modelName.trim());
                    }
                }
            }
            return model;
        }

    public String replaceAll (String value, Map< String, String > replacementMap,
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



}


