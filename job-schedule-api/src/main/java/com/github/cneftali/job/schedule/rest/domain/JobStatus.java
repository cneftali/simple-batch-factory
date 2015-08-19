package com.github.cneftali.job.schedule.rest.domain;

public enum JobStatus {
    WAITING(0),
    SCHED_PROCESSING(1),
    JOB_PROCESSING(2);

    int value;
    private static final JobStatus[] JOB_STATUSES = values();

    private JobStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static JobStatus getByValue(int value) {
        for (JobStatus p : JOB_STATUSES) {
            if (p.value == value) {
                return p;
            }
        }
        return null;
    }
}
