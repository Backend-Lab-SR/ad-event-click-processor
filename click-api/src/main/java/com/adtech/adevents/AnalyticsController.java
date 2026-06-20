package com.adtech.adevents;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final ClickAnalyticsRepository clickAnalyticsRepository;

    public AnalyticsController(ClickAnalyticsRepository clickAnalyticsRepository) {
        this.clickAnalyticsRepository = clickAnalyticsRepository;
    }

    @GetMapping("/top-ads")
    public List<TopAdResult> topAds() {
        return clickAnalyticsRepository.findTopAds().stream()
                .map(view -> new TopAdResult(view.getAdId(), view.getClickCount()))
                .toList();
    }
}
