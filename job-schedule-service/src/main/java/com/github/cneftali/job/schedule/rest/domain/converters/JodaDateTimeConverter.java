package com.github.cneftali.job.schedule.rest.domain.converters;


import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.DateTime;

/**
 * Joda DateTime <-> JPA 2.1 converter
 */
@Converter(autoApply = true)
public class JodaDateTimeConverter implements AttributeConverter<DateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(DateTime dateTime) {
        return dateTime.toDate();
    }

    @Override
    public DateTime convertToEntityAttribute(Date date) {
        return new DateTime(date);
    }
}
