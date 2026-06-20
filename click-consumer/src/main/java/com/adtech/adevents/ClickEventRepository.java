package com.adtech.adevents;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickEventRepository extends JpaRepository<ClickEventEntity, Long> {
}
