package com.tigger.product.performance.dao;

import com.tigger.product.performance.dto.TimeTableDTO;
import java.time.LocalTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TimeTableMapper {

    /**
     * 장소, 시간이 중복되는 공연의 카운트를 리턴한다(중복 기준 : 장소코드, 타임테이블코드)
     *
     * @param venueCode : 장소코드
     * @param date      : 공연일자
     * @param startTime : 공연시작시간
     * @param endTime   : 공연종료시간
     * @return int : 중복 공연 카운트
     */
    int getDupTimeAndVenueCnt(int venueCode, String date, LocalTime startTime, LocalTime endTime);

    /**
     * 특정 타임테이블의 상태값을 리턴한다
     *
     * @param timeId : 타임테이블 ID
     * @return : 해당 타임 상태값
     */
    String getCurrentState(int timeId);

    /**
     * 타임테이블에 대한 결제 건수를 리턴한다
     *
     * @param timeId : 타임테이블 ID
     * @return : 결제 건수
     */
    int chkExistPaymentCnt(int timeId);

    /**
     * 타임테이블의 상태값을 변경한다
     *
     * @param timeId : 타임테이블 ID
     */
    void updateState(int timeId);

    /**
     * @param timeList : 등록할 타임테이블 list
     * @return int : 등록 타임테이블 건수
     */
    int insertTimeTableList(List<TimeTableDTO> timeList);
}
