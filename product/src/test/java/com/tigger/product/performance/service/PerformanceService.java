package com.tigger.product.performance.service;

import com.tigger.product.performance.dao.PerformanceMapper;
import com.tigger.product.performance.dto.PerformanceDTO;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.DupPerformanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceMapper pMapper;

    /**
     * 업체코드와 업체상품코드로 중복된 공연인지 체크한다
     * @param dto
     */
    public void chkDupPerformance(PerformanceDTO dto) {
        int dupCnt = pMapper.getDupPerformanceCnt(dto.getEnterpriseCode(), dto.getEnterprisePid());
        if (dupCnt > 0) {
            throw new DupPerformanceException("중복된 공연이 존재합니다");
        }
    }

    /**
     * 신규 공연 입력
     * @param performance
     */
    public void insertPerformance(PerformanceDTO performance) {
        int result = pMapper.insertPerformance(performance);
        if (result == 0) {
            throw new CustomRuntimeException("insertPerformance exception 발생");
        }
    }

    /**
     * 공연 조회(id 기준)
     * @param id
     * @return
     */
    public PerformanceDTO getPerformanceById(int id) {
        return pMapper.getById(id);
    }

    /**
     * 공연 조회(공연명 기준)
     * @param name
     * @return
     */
    public PerformanceDTO getPerformanceByName(String name) {
        return pMapper.getByName(name);
    }

    /**
     * 공연 삭제
     * @param id
     */
    public void deletePerformance(int id) {
        int result = pMapper.deletePerformance(id);
        if (result == 0) {
            throw new CustomRuntimeException("deletePerformance exception 발생");
        }
    }
}
