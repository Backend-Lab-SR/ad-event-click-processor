package com.adtech.adevents;

import java.time.Instant;
import java.util.UUID;

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

    @PostMapping
    public ResponseEntity<Void> recordClick(@RequestBody ClickRequest request) {
        ClickEvent event = new ClickEvent(
                UUID.randomUUID().toString(),
                request.adId(),
                request.userId(),
                Instant.now()
        );
        log.info("Generated click event: {}", event);
        return ResponseEntity.accepted().build();
    }
}
