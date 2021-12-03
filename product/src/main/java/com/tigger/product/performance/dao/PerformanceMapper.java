package com.tigger.product.performance.dao;

import com.tigger.product.performance.dto.PerformanceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PerformanceMapper {

    /**
     * DB에서 파라미터와 중복되는 공연의 카운트를 리턴한다(중복 기준 : 업체코드, 업체고유공연코드)
     * @param enterCode
     * @param pidCode
     * @return
     */
    int getDupPerformanceCnt(int enterCode, String pidCode);

    /**
     * 공연을 등록한다
     * @param performance
     * @return
     */
    int insertPerformance(PerformanceDTO performance);

    /**
     * id 로 공연정보를 조회한다
     * @param id
     * @return
     */
    PerformanceDTO getById(int id);

    /**
     * 공연명 으로 공연정보를 조회한다
     * @param name
     * @return
     */
    PerformanceDTO getByName(String name);

    /**
     * 현재 공연의 상태값을 리턴한다
     * @param id
     * @return
     */
    String getCurrentFlag(int id);

    /**
     * 특정 공연의 결제 건수를 리턴한다
     * @param id
     * @return
     */
    int chkExistPaymentCnt(int id);

    /**
     * 공연 상태값을 변경한다
     * @param id
     */
    void updateFlag(int id);
}
