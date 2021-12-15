package com.tigger.product.performance.dao;

import java.time.LocalTime;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TimeTableMapper {

    /**
     * DB에서 파라미터와 중복되는 공연의 카운트를 리턴한다(중복 기준 : 장소코드, 타임테이블코드)
     * @param venueCode
     * @param date
     * @param startTime
     * @param endTime
     * @return
     */
    int getDupTimeAndVenueCnt(int venueCode, String date, LocalTime startTime, LocalTime endTime);

    /**
     * 특정 타임테이블의 상태값을 리턴한다
     * @param timeId
     * @return
     */
    String getCurrentFlag(int timeId);

    /**
     * 타임테이블에 대한 결제 건수를 리턴한다
     * @param timeId
     * @return
     */
    int chkExistPaymentCnt(int timeId);

    /**
     * 타임테이블의 상태값을 변경한다
     * @param timeId
     */
    void updateFlag(int timeId);
}
