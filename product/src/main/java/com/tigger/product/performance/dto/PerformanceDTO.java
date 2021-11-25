package com.tigger.product.performance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerformanceDTO {

    private int no;
    private String name;
    private int categoryCode;
    private int enterpriseCode;
    private String enterprisePid;
    private String startDate;
    private String endDate;
    private int venueCode;
    private String info;
    private double commissionRate;
    private int runningTime;
    private String DrawDate;
    private char state;

}
