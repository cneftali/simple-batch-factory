package com.github.cneftali.job.commons.batch;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joda.time.DateTime;
import org.springframework.batch.core.JobParameters;

public class JobLaunchRequest implements Serializable {

    private static final long serialVersionUID = 6812990125800596798L;

    private final String jobName;
    private final JobParameters jobParameters;
    private final Long scheduleId;
    private final DateTime createTime;

    /**
     * @param jobName       job name to be launched
     * @param jobParameters parameters to run the job with
     * @param scheduleId
     */
    @JsonCreator
    public JobLaunchRequest(@JsonProperty("jobName") final String jobName,
                            @JsonProperty("jobParameters") final JobParameters jobParameters,
                            @JsonProperty("scheduleId") final Long scheduleId,
                            @JsonProperty("createTime") final DateTime createTime) {
        super();
        this.jobName = jobName;
        this.jobParameters = jobParameters;
        this.scheduleId = scheduleId;
        this.createTime = createTime == null ? DateTime.now() : createTime;
    }

    /**
     * @return the jobName to be executed
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @return the {@link JobParameters} for this request
     */
    public JobParameters getJobParameters() {
        return this.jobParameters;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, JSON_STYLE);
    }
}