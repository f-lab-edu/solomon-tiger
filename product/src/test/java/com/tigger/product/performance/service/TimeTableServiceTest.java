package com.tigger.product.performance.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.tigger.product.performance.dao.PerformanceMapper;
import com.tigger.product.performance.dao.TimeTableMapper;
import com.tigger.product.performance.dto.TimeTableDTO;
import com.tigger.product.performance.enums.PerformanceState;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.DupTimeAndVenueException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeTableServiceTest {

    @Mock
    TimeTableMapper timeMapper;
    @Mock
    PerformanceMapper pMapper;
    @InjectMocks
    TimeTableService timeService;
    @InjectMocks
    PerformanceService pService;

    @Test
    @DisplayName("공연 등록 예외 (중복 일시/장소 체크) : 중복 일시/장소 카운트 > 0 일 때, DupTimeAndVenueException 발생시킨다")
    public void addDupTimeAndVenuePerformance() {
        int venueCode = 1;
        String date = "20211121";
        int hour = 17;
        int minute = 30;
        int runningTime = 120;

        when(this.timeMapper.getDupTimeAndVenueCnt(anyInt(), anyString(), any(), any())).thenReturn(1);

        assertThrows(DupTimeAndVenueException.class, () -> {
            this.timeService.chkDupTimeAndVenue(venueCode, date, hour, minute, runningTime);
        });
    }

    @Test
    @DisplayName("공연 특정 타임 상태값 변경시(정상 -> 취소), 결제 건수 있으면 예외 발생")
    public void updateTimeFlag_결제_존재() {
        when(this.timeMapper.getCurrentState(anyInt())).thenReturn(PerformanceState.NORMAL.getValue());
        when(this.timeMapper.chkExistPaymentCnt(anyInt())).thenReturn(1);

        assertThrows(CustomRuntimeException.class, () -> {
            this.timeService.updateFlag(1);
        });
    }

    @Test
    @DisplayName("등록한 공연의 타임 테이블 리스트 등록 성공")
    public void registerTimeTableList() {
        int performanceId = 1;
        List<TimeTableDTO> timeList = this.makeTimeTableListForSpecificPerformance(anyInt());
        when(this.timeMapper.insertTimeTableList(timeList)).thenReturn(3);

        assertDoesNotThrow(() -> {
            this.timeService.registerTimeTableList(performanceId, timeList);
        });
    }

    private List<TimeTableDTO> makeTimeTableListForSpecificPerformance(int performanceId) {
        List<TimeTableDTO> timeList = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            TimeTableDTO dto = TimeTableDTO.builder()
                .performanceId(performanceId)
                .date("20211216")
                .startTime(LocalTime.of(15 + i, 30))
                .build();
            timeList.add(dto);
        }
        return timeList;
    }


}
