package com.adtech.adevents;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "click_events")
public class ClickEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String adId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Instant eventTimestamp;

    @Column(nullable = false)
    private Instant processedTimestamp;

    public static ClickEventEntity from(ClickEvent event) {
        ClickEventEntity entity = new ClickEventEntity();
        entity.eventId = event.eventId();
        entity.adId = event.adId();
        entity.userId = event.userId();
        entity.eventTimestamp = event.eventTimestamp();
        entity.processedTimestamp = Instant.now();
        return entity;
    }

    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAdId() {
        return adId;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }

    public Instant getProcessedTimestamp() {
        return processedTimestamp;
    }
}
