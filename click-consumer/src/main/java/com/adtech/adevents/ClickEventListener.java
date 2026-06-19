package com.adtech.adevents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClickEventListener {

    private static final Logger log = LoggerFactory.getLogger(ClickEventListener.class);

    private final ObjectMapper objectMapper;

    public ClickEventListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SqsListener("click-events")
    public void consume(String message) {
        log.info("Received click event: {}", deserialize(message));
    }

    private ClickEvent deserialize(String message) {
        try {
            return objectMapper.readValue(message, ClickEvent.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize click event", e);
        }
    }
}
