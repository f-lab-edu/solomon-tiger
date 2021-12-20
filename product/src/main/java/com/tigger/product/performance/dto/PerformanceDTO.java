package com.tigger.product.performance.dto;

import lombok.Builder;
import lombok.Getter;


public class PerformanceDTO {

    private final Integer no;
    private final String name;
    private final Integer categoryCode;
    @Getter
    private final Integer enterpriseCode;
    @Getter
    private final String enterprisePid;
    private final String startDate;
    private final String endDate;
    private final Integer venueCode;
    private final String info;
    private final double commissionRate;
    private final int runningTime;
    private final String drawDate;
    private Character state;
    private String createdAt;
    private String updatedAt;

    @Builder
    public PerformanceDTO(Integer no, String name, Integer categoryCode, Integer enterpriseCode, String enterprisePid,
        String startDate, String endDate, Integer venueCode, String info, double commissionRate, int runningTime, String drawDate) {
        this.no = no;
        this.name = name;
        this.categoryCode = categoryCode;
        this.enterpriseCode = enterpriseCode;
        this.enterprisePid = enterprisePid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venueCode = venueCode;
        this.info = info;
        this.commissionRate = commissionRate;
        this.runningTime = runningTime;
        this.drawDate = drawDate;
    }
}
