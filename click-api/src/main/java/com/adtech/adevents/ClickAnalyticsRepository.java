package com.adtech.adevents;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClickAnalyticsRepository extends JpaRepository<ClickEventEntity, Long> {

    @Query("""
            SELECT e.adId AS adId, COUNT(e) AS clickCount
            FROM ClickEventEntity e
            GROUP BY e.adId
            ORDER BY clickCount DESC
            LIMIT 10
            """)
    List<TopAdView> findTopAds();
}
