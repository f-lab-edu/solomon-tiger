package com.tigger.product.performance.dto;

import java.time.LocalTime;
import lombok.Builder;

public class TimeTableDTO {

    private final Integer id;
    private final Integer performanceId;
    private final String date;
    private final LocalTime startTime;
    private String createdAt;
    private String updatedAt;

    @Builder
    public TimeTableDTO(Integer id, Integer performanceId, String date, LocalTime startTime) {
        this.id = id;
        this.performanceId = performanceId;
        this.date = date;
        this.startTime = startTime;
    }
}
