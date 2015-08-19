package com.github.cneftali.job.schedule.rest.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.json.JSONException;
import org.json.JSONObject;

@Converter(autoApply = true)
public class JSONObjectConvertor implements AttributeConverter<JSONObject, String> {

    @Override
    public String convertToDatabaseColumn(final JSONObject attribute) {
        return attribute.toString();
    }

    @Override
    public JSONObject convertToEntityAttribute(final String dbData) {
        try {
            return new JSONObject(dbData);
        } catch (JSONException e) {
           throw new IllegalArgumentException("Cannot convert String to JSONObject", e);
        }
    }
}
