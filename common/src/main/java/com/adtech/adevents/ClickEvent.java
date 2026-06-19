package com.adtech.adevents;

import java.time.Instant;

public record ClickEvent(
        String eventId,
        String adId,
        String userId,
        Instant eventTimestamp
) {
}
