package com.tigger.product.performance.service;

import com.tigger.product.performance.dao.TimeTableMapper;
import com.tigger.product.performance.enums.PerformanceFlag;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.DupTimeAndVenueException;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableMapper timeMapper;

    /**
     * 공연장소코드와 공연일자, 공연시작시간, 공연종료시간 데이터로 일시/장소에 대한 중복을 체크한다
     *
     * @param venueCode
     * @param date
     * @param hour
     * @param minute
     * @param runningTime
     */
    public void chkDupTimeAndVenue(int venueCode, String date, int hour, int minute, int runningTime) {
        /*
            시간 연산 메소드 사용 위해서 java.util.Date & SimpleDateFormat 조합 대신
            java.time.LocalTime 사용
         */
        LocalTime startTime = LocalTime.of(hour, minute);
        LocalTime endTime = startTime.plusMinutes(runningTime);
        int dupCnt = timeMapper.getDupTimeAndVenueCnt(venueCode, date, startTime, endTime);
        if (dupCnt > 0) {
            throw new DupTimeAndVenueException();
        }
    }

    /**
     * timeTable 의 상태값 변경( 정상등록 -> 취소 )
     *
     * @param timeId
     */
    public void updateFlag(int timeId) {
        if (timeMapper.getCurrentFlag(timeId).equals(PerformanceFlag.CANCEL.getValue())) {
            throw new IllegalArgumentException("취소 공연 상태값 변경 불가");
        }
        if (timeMapper.chkExistPaymentCnt(timeId) > 0) {
            throw new CustomRuntimeException("유효 결제건 존재하여 취소 불가");
        }
        timeMapper.updateFlag(timeId);
    }
}
