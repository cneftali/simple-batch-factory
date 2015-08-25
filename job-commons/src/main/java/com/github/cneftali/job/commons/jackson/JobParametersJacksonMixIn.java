package com.github.cneftali.job.commons.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties("empty")
public abstract class JobParametersJacksonMixIn {

    @JsonProperty
    abstract boolean isEmpty();
}