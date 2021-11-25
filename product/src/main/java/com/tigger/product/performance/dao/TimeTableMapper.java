package com.tigger.product.performance.dao;

import java.time.LocalTime;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TimeTableMapper {

    int getDupTimeAndVenueCnt(int venueCode, String date, LocalTime startTime, LocalTime endTime);

    String getCurrentFlag(int timeId);

    int chkExistPaymentCnt(int timeId);

    void updateFlag(int timeId);
}
