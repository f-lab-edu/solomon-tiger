package com.tigger.product.performance.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.tigger.product.performance.dao.TimeTableMapper;
import com.tigger.product.performance.enums.PerformanceFlag;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.DupTimeAndVenueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeTableServiceTest {

    @Mock
    TimeTableMapper tMapper;
    @InjectMocks
    TimeTableService timeService;

    @Test
    @DisplayName("공연 등록 예외 (중복 일시/장소 체크) : 중복 일시/장소 카운트 > 0 일 때, DupTimeAndVenueException 발생시킨다")
    public void addDupTimeAndVenuePerformance() {
        int venueCode = 1;
        String date = "20211121";
        int hour = 17;
        int minute = 30;
        int runningTime = 120;

        when(tMapper.getDupTimeAndVenueCnt(anyInt(), anyString(), any(), any())).thenReturn(1);

        assertThrows(DupTimeAndVenueException.class, () -> {
            timeService.chkDupTimeAndVenue(venueCode, date, hour, minute, runningTime);
        });
    }

    @Test
    @DisplayName("공연 특정 타임 상태값 변경시(정상 -> 취소), 결제 건수 있으면 예외 발생")
    public void updateTimeFlag_결제_존재() {
        when(tMapper.getCurrentFlag(anyInt())).thenReturn(PerformanceFlag.NORMAL.getValue());
        when(tMapper.chkExistPaymentCnt(anyInt())).thenReturn(1);

        assertThrows(CustomRuntimeException.class, () -> {
            timeService.updateFlag(1);
        });
    }

}
