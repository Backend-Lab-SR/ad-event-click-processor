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

    public String getAdId() {
        return adId;
    }
}
