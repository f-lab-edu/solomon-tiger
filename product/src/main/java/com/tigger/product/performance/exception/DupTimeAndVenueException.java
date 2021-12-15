package com.tigger.product.performance.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DupTimeAndVenueException extends RuntimeException {

    public DupTimeAndVenueException() {
        super("해당 장소에 등록되어 있는 공연 스케줄과 중복됩니다");
    }
}
