package com.github.cneftali.job.commons.jackson;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;

public class JobParameterJacksonDeserializer extends JsonDeserializer<JobParameter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobParameterJacksonDeserializer.class);

    @Override
    public JobParameter deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
            throws IOException {
        final ObjectCodec oc = jsonParser.getCodec();
        final JsonNode node = oc.readTree(jsonParser);

        final String value = node.get("value").asText();
        final boolean identifying = node.get("identifying").asBoolean();
        final String type = node.get("type").asText();

        final JobParameter jobParameter;

        if (! "STRING".equalsIgnoreCase(type)) {
            if ("DATE".equalsIgnoreCase(type)) {
                if (isNumeric(value)) {
                    jobParameter = new JobParameter(new Date(Long.valueOf(value)), identifying);
                } else {
                    try {
                        final SimpleDateFormat iso8601dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        jobParameter = new JobParameter(iso8601dateformat.parse(value), identifying);
                    } catch (final ParseException e) {
                        throw new IOException(e);
                    }
                }
            } else if ("DOUBLE".equalsIgnoreCase(type)) {
                jobParameter = new JobParameter(Double.valueOf(value), identifying);
            } else if ("LONG".equalsIgnoreCase(type)) {
                jobParameter = new JobParameter(Long.valueOf(value), identifying);
            } else {
                throw new IllegalStateException("Unsupported JobParameter type: " + type);
            }
        } else {
            jobParameter = new JobParameter(value, identifying);
        }

        LOGGER.debug("jobParameter - value: {} (type: {}, isIdentifying: {})", jobParameter.getValue(),
                     jobParameter.getType().name(),
                     jobParameter.isIdentifying());

        return jobParameter;
    }
}