package com.tigger.product.performance.service;

import com.tigger.product.performance.dao.PerformanceMapper;
import com.tigger.product.performance.dto.PerformanceDTO;
import com.tigger.product.performance.enums.PerformanceState;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.DupPerformanceException;
import com.tigger.product.performance.exception.NonExistentPerformanceException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceMapper pMapper;

    /**
     * 업체코드와 업체상품코드로 중복된 공연인지 체크한다
     *
     * @param dto
     */
    public void chkDupPerformance(PerformanceDTO dto) {
        Assert.notNull(dto.getEnterpriseCode(), "업체코드가 존재하지 않습니다");
        Assert.notNull(dto.getEnterprisePid(), "업체상품코드가 존재하지 않습니다");
        int dupCnt = this.pMapper.getDupPerformanceCnt(dto.getEnterpriseCode(), dto.getEnterprisePid());
        if (dupCnt > 0) {
            throw new DupPerformanceException();
        }
    }

    /**
     * 신규 공연 등록
     *
     * @param performance
     */
    public void registerPerformance(PerformanceDTO performance) {
        Assert.notNull(performance, "Performance is null");
        int result = this.pMapper.insertPerformance(performance);
        if (result == 0) {
            throw new CustomRuntimeException("registerPerformance exception 발생");
        }
    }

    /**
     * 공연 조회(id 기준)
     *
     * @param id : 공연 ID
     * @return PerformanceDTO
     */
    public PerformanceDTO getPerformanceById(int id) {
        Assert.notNull(id, "ID is null");
        PerformanceDTO result = this.pMapper.getById(id);
        if (result == null) {
            throw new NonExistentPerformanceException();
        }
        return result;
    }

    /**
     * 공연 조회(공연명 기준)
     *
     * @param name : 공연명 검색 키워드
     * @return List<PerformanceDTO> : 검색 결과 리스트
     */
    private List<PerformanceDTO> getPerformanceListByName(String name) {
        return this.pMapper.getListByName(name);
    }

    /**
     * 공연 상태값 변경( 정상등록 -> 취소 )
     *
     * @param id : 공연 ID
     */
    public void updateState(int id) {
        Assert.notNull(id, "ID is null");
        if (PerformanceState.CANCEL.getValue().equals(this.pMapper.getCurrentState(id))) {
            throw new IllegalArgumentException("취소 공연 상태값 변경 불가");
        }
        if (this.pMapper.chkExistPaymentCnt(id) > 0) {
            throw new CustomRuntimeException("유효 결제건 존재하여 취소 불가");
        }
        this.pMapper.updateState(id);
        this.validForUpdateState(id);
    }

    /**
     * updateState 결과에 대한 DB 데이터 검증
     *
     * @param id : 공연 ID
     */
    private void validForUpdateState(int id) {
        String dbVal = this.pMapper.getCurrentState(id);
        if (!PerformanceState.CANCEL.getValue().equals(dbVal)) {
            throw new CustomRuntimeException("공연 상태값 변경이 정상적으로 이루어지지 않았습니다");
        }
    }

}
