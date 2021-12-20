package com.tigger.product.performance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PerformanceState {
    NORMAL("정상공연", "0"),
    CANCEL("취소공연", "1"),
    END("종료공연", "2");

    private String state;
    private String value;
}
