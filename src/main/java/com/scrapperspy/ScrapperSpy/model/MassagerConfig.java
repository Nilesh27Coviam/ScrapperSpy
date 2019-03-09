package com.scrapperspy.ScrapperSpy.model;

import com.scrapperspy.ScrapperSpy.FieldNames;
import com.scrapperspy.ScrapperSpy.dao.GenericRegexDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
@Setter
@Document(collection = MassagerConfig.COLLECTION_NAME)
public class MassagerConfig {
        public static final String COLLECTION_NAME = "MASSAGER_CONFIG";

        @Field(value = FieldNames.SOURCE)
        private String source;

        @Field(value = FieldNames.CATEGORY)
        private String category;

        @Field(value = FieldNames.ATTRIBUTE_NAME)
        private String attributeName;

        @Field(value = FieldNames.FIELD_NAME)
        private String fieldName;

        @Field(value = FieldNames.PRIORITY)
        private int priority;

        @Field(value = FieldNames.LOOKUP_KEY)
        private String lookupKey;

        @Field(value = FieldNames.LOOKUP_REGEX)
        private List<String> lookupRegex;

        @Field(value = FieldNames.TRANSFORMATION_REGEX)
        private List<String> transformationRegex;

        @Field(value = FieldNames.CUSTOM_CLASS)
        private String customClass;

        @Field(value = FieldNames.DEFAULT_VALUE)
        private String defaultValue;

/*
    @Field(value = FieldNames.TRANSFORMATION_CALLS)
    private List<TransformationLogic> transformationCalls;
*/

        @Field(value = FieldNames.REPLACEMENT_MAP)
        private Map<String, String> replacementMap;

        @Field(value = FieldNames.IS_SYNONYM_PRESENT)
        private boolean isSynonymPresent;

        @Field(value = FieldNames.MULTI_VALUE_SEPARATOR)
        private String multiValueSeparator;

        @Field(value = FieldNames.IS_INVALID_CHECK)
        private boolean isInvalidCheck;

        @Field(value = FieldNames.IS_REPLACEMENT_NEEDED)
        private boolean isReplacementNeeded;

        @Field(value = FieldNames.EXTRACTION_LOGIC)
        private String extractionLogic;

    }

