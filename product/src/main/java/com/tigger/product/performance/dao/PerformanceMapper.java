package com.tigger.product.performance.dao;

import com.tigger.product.performance.dto.PerformanceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PerformanceMapper {

    int getDupPerformanceCnt(int enterCode, String pidCode);

    int insertPerformance(PerformanceDTO performance);

    PerformanceDTO getById(int id);

    PerformanceDTO getByName(String name);

    String getCurrentFlag(int id);

    int chkExistPaymentCnt(int id);

    void updateFlag(int id);
}
