package com.github.cneftali.job.schedule.rest.domain;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.json.JSONObject;

@Entity(name = "JobRequest")
@Table(name = "SCHED_JOB_REQUEST")
@Access(FIELD)
@SequenceGenerator(sequenceName = "ID_SCHED_JOB_REQUEST_SEQ",
                   name = "ID_SCHED_JOB_REQUEST_SEQ_GEN",
                   initialValue = 1,
                   allocationSize = 1)
@NamedQueries({
                      @NamedQuery(name = "JobRequest.deleteById",
                                  query = "DELETE FROM JobRequest s WHERE s.id = :id AND s.jobStatus in (:status)")
              })
public class JobRequest implements Serializable {

    private static final long serialVersionUID = 7282205970147353286L;

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = SEQUENCE, generator = "ID_SCHED_JOB_REQUEST_SEQ_GEN")
    private Long id;

    @Column(name = "JOB_NAME", updatable = false, length = 100, nullable = false)
    private String jobName;

    @Column(name = "JOB_PARAMETER", updatable = false, length = 2000, nullable = false)
    private JSONObject jobParameter;

    @Column(name = "POD_ID", updatable = false, nullable = false)
    private Long podId;

    @Column(name = "DATE_START", updatable = false, nullable = false)
    private DateTime dateStart;

    @Column(name = "CLIENT_ID", updatable = false, nullable = false)
    private Long clientId;

    @Column(name = "STATUS", nullable = false)
    private JobStatus jobStatus = JobStatus.WAITING;

    public JobRequest() {
    }

    public JobRequest(final String jobName,
                      final JSONObject jobParameter,
                      final Long podId,
                      final Long clientId,
                      final DateTime dateStart) {
        this();
        this.jobName = jobName;
        this.jobParameter = jobParameter;
        this.podId = podId;
        this.clientId = clientId;
        this.dateStart = dateStart;
    }

    public Long getId() {
        return id;
    }

    public String getJobName() {
        return jobName;
    }

    public JSONObject getJobParameter() {
        return jobParameter;
    }

    public Long getPodId() {
        return podId;
    }

    public DateTime getDateStart() {
        return dateStart;
    }

    public Long getClientId() {
        return clientId;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(final JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
