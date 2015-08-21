package com.github.cneftali.job.schedule.rest.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.github.cneftali.job.schedule.rest.domain.JobStatus;

@Converter(autoApply = true)
public class JobStatusConverter implements AttributeConverter<JobStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(final JobStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public JobStatus convertToEntityAttribute(final Integer dbData) {
        return JobStatus.getByValue(dbData);
    }
}
