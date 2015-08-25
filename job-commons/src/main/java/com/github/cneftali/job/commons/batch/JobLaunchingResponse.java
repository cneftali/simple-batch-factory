package com.github.cneftali.job.commons.batch;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class JobLaunchingResponse implements Serializable {

    private static final long serialVersionUID = -6869985379779788160L;

    private final long startTime;
    private final long endTime;
    private final int slaveId;
    private final String jobName;

    @JsonCreator
    public JobLaunchingResponse(@JsonProperty("startTime") final long startTime,
                                @JsonProperty("endTime") final long endTime,
                                @JsonProperty("slaveId") final int slaveId,
                                @JsonProperty("jobName")  final String jobName) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.slaveId = slaveId;
        this.jobName = jobName;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public int getSlaveId() {
        return this.slaveId;
    }

    public String getJobName() {
        return this.jobName;
    }


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, JSON_STYLE);
    }
}

