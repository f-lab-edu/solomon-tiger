package com.tigger.product.performance.dao;

import com.tigger.product.performance.dto.PerformanceDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PerformanceMapper {

    /**
     * DB에서 파라미터와 중복되는 공연의 카운트를 리턴한다(중복 기준 : 업체코드, 업체고유공연코드)
     *
     * @param enterCode : 업체코드
     * @param pidCode   : 업체고유공연코드
     * @return int : 중복되는 공연 카운트
     */
    int getDupPerformanceCnt(int enterCode, String pidCode);

    /**
     * 공연을 등록한다
     *
     * @param performance
     * @return int : 등록 공연 건수
     */
    int insertPerformance(PerformanceDTO performance);

    /**
     * id 로 공연정보를 조회한다
     *
     * @param id : 공연 ID
     * @return PerformanceDTO
     */
    PerformanceDTO getById(int id);

    /**
     * 공연명 으로 공연정보를 조회한다
     * @param name
     * @return List<PerformanceDTO> : 조회 결과 List
     */
    List<PerformanceDTO> getListByName(String name);

    /**
     * 현재 공연의 상태값을 리턴한다
     *
     * @param id
     * @return String : 공연상태값
     */
    String getCurrentState(int id);

    /**
     * 특정 공연의 결제 건수를 리턴한다
     *
     * @param id : 공연 ID
     * @return int : 공연 결제 건수
     */
    int chkExistPaymentCnt(int id);

    /**
     * 공연 상태값을 변경한다 (정상 -> 취소)
     *
     * @param id : 공연 ID
     */
    void updateState(int id);
}
