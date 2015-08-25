package com.github.cneftali.job.commons.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = JobParameterJacksonDeserializer.class)
public abstract class JobParameterJacksonMixIn {

}