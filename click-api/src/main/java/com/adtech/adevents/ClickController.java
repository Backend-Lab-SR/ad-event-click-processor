package com.adtech.adevents;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clicks")
public class ClickController {

    private static final Logger log = LoggerFactory.getLogger(ClickController.class);
    private static final String CLICK_EVENTS_QUEUE = "click-events";

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;

    public ClickController(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Void> recordClick(@RequestBody ClickRequest request) {
        ClickEvent event = new ClickEvent(
                UUID.randomUUID().toString(),
                request.adId(),
                request.userId(),
                Instant.now()
        );
        log.info("Generated click event: {}", event);
        sqsTemplate.send(CLICK_EVENTS_QUEUE, serialize(event));
        return ResponseEntity.accepted().build();
    }

    private String serialize(ClickEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize click event", e);
        }
    }
}
