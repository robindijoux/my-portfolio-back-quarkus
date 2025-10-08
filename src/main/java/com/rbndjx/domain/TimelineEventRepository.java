package com.rbndjx.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TimelineEventRepository implements PanacheRepositoryBase<TimelineEvent, String> {
    
    public List<TimelineEvent> findAllOrderedByTimestamp() {
        return list("ORDER BY timestamp DESC");
    }
    
    public List<TimelineEvent> findByType(TimelineEventType type) {
        return list("type", type);
    }
}
