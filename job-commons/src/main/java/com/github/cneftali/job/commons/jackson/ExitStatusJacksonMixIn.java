package com.github.cneftali.job.commons.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson MixIn for {@link org.springframework.batch.core.ExitStatus} de-serialization.
 *
 * @author Gunnar Hillert
 * @since 2.0
 */
public abstract class ExitStatusJacksonMixIn {

    ExitStatusJacksonMixIn(@JsonProperty("exitCode") String exitCode,
                           @JsonProperty("exitDescription") String exitDescription) {
    }

    @JsonProperty
    abstract boolean isRunning();
}