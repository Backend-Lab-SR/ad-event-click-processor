package com.adtech.adevents;

import java.time.Duration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClickEventListener {

    private static final Logger log = LoggerFactory.getLogger(ClickEventListener.class);
    private static final Duration DEDUP_TTL = Duration.ofHours(24);

    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;
    private final ClickEventRepository clickEventRepository;

    public ClickEventListener(
            ObjectMapper objectMapper,
            StringRedisTemplate redisTemplate,
            ClickEventRepository clickEventRepository) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.clickEventRepository = clickEventRepository;
    }

    @SqsListener("click-events")
    public void consume(String message) {
        ClickEvent event = deserialize(message);
        String dedupKey = "click:" + event.adId() + ":" + event.userId();

        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(dedupKey, "1", DEDUP_TTL);
        if (Boolean.FALSE.equals(isNew)) {
            log.info("Duplicate click event skipped: {}", event);
            return;
        }

        log.info("Received click event: {}", event);
        clickEventRepository.save(ClickEventEntity.from(event));
    }

    private ClickEvent deserialize(String message) {
        try {
            return objectMapper.readValue(message, ClickEvent.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize click event", e);
        }
    }
}
