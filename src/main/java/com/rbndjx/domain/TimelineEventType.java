package com.rbndjx.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TimelineEventType {
    EDUCATION,
    ACHIEVEMENT,
    WORK;

    @JsonCreator
    public static TimelineEventType fromString(String value) {
        if (value == null) {
            return null;
        }
        
        try {
            return TimelineEventType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid TimelineEventType: " + value + 
                ". Valid values are: EDUCATION, ACHIEVEMENT, WORK");
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
