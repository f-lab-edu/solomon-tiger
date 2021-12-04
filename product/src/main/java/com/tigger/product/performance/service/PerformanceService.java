package com.tigger.product.performance.service;

import com.tigger.product.performance.dao.PerformanceMapper;
import com.tigger.product.performance.dto.PerformanceDTO;
import com.tigger.product.performance.enums.PerformanceFlag;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.DupPerformanceException;
import com.tigger.product.performance.exception.NonExistentPerformanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        int dupCnt = pMapper.getDupPerformanceCnt(dto.getEnterpriseCode(), dto.getEnterprisePid());
        if (dupCnt > 0) {
            throw new DupPerformanceException();
        }
    }

    /**
     * 신규 공연 입력
     *
     * @param performance
     */
    public void registerPerformance(PerformanceDTO performance) {
        int result = pMapper.insertPerformance(performance);
        if (result == 0) {
            throw new CustomRuntimeException("registerPerformance exception 발생");
        }
    }

    /**
     * 공연 조회(id 기준)
     *
     * @param id
     * @return
     */
    public PerformanceDTO getPerformanceById(int id) {
        PerformanceDTO result = pMapper.getById(id);
        if (result == null) {
            throw new NonExistentPerformanceException();
        }
        return result;
    }

    /**
     * 공연 조회(공연명 기준)
     *
     * @param name
     * @return
     */
    public PerformanceDTO getPerformanceByName(String name) {
        return pMapper.getByName(name);
    }

    /**
     * 공연 상태값 변경( 정상등록 -> 취소 )
     *
     * @param id
     */
    public void updateFlag(int id) {
        if (PerformanceFlag.CANCEL.getValue().equals(pMapper.getCurrentFlag(id))) {
            throw new IllegalArgumentException("취소 공연 상태값 변경 불가");
        }
        if (pMapper.chkExistPaymentCnt(id) > 0) {
            throw new CustomRuntimeException("유효 결제건 존재하여 취소 불가");
        }
        pMapper.updateFlag(id); //해당 공연 취소
    }

}
