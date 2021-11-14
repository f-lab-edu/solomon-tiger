package com.tigger.product.performance;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tigger.product.performance.dao.PerformanceMapper;
import com.tigger.product.performance.exception.DupPerformanceException;
import com.tigger.product.performance.service.PerformanceService;
import com.tigger.product.performance.dto.PerformanceDTO;
import com.tigger.product.performance.exception.CustomRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
    1. @ExtendWith({MockitoExtension.class}) : 테스트 클래스가 Mockito를 사용함을 의미
    2. @Mock : Mock 객체를 생성
 */

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    PerformanceDTO performanceDTO;
    @Mock
    PerformanceMapper pMapper;
    @InjectMocks
    PerformanceService pService;

    public PerformanceDTO generatePerformance() {
        PerformanceDTO newPerformanceDTO = PerformanceDTO.builder()
            .no(1)
            .name("test")
            .build();
        return newPerformanceDTO;
    }

    @Test
    @DisplayName("공연 등록 예외 : 중복공연 카운트 > 0 일 때, DupPerformanceException 발생시킨다")
    public void addDupPerformance() {
        // given
        int enterCode = performanceDTO.getEnterpriseCode();
        String pid = performanceDTO.getEnterprisePid();
        // when
        when(pMapper.getDupPerformanceCnt(enterCode, pid)).thenReturn(1);
        // then
        Assertions.assertThrows(DupPerformanceException.class, () -> {
            pService.chkDupPerformance(performanceDTO);
        });
    }

    @Test
    @DisplayName("공연 등록 정상 : 중복공연 카운트 0 일 때 예외 발생하지 않는다")
    public void addNotDupPerformance() {
        // given
        int enterCode = performanceDTO.getEnterpriseCode();
        String pid = performanceDTO.getEnterprisePid();
        // when
        when(pMapper.getDupPerformanceCnt(enterCode, pid)).thenReturn(0);
        // then
        Assertions.assertDoesNotThrow(() -> {
            pService.chkDupPerformance(performanceDTO);
        });
    }

    @Test
    public void insertPerformance_공연_등록_성공() {
        // given
        PerformanceDTO newDto = this.generatePerformance();
        // when
        when(pMapper.insertPerformance(any())).thenReturn(1);
        // then
        Assertions.assertDoesNotThrow(() -> {
            pService.insertPerformance(newDto);
        });
    }

    @Test
    @DisplayName("mapper 수행 결과 0건 일때 runtime exception 발생시킨다")
    public void insertPerformance_공연_등록_실패() {
        // given
        PerformanceDTO newDto = this.generatePerformance();
        // when
        when(pMapper.insertPerformance(any())).thenReturn(0);
        // then
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            pService.insertPerformance(newDto);
        });
    }

    @Test
    public void getPerformanceById_공연_조회_성공() {
        // given
        PerformanceDTO target = this.generatePerformance();
        // when
        when(pMapper.getById(1)).thenReturn(target);
        when(pMapper.getById(999)).thenReturn(null);
        when(pMapper.getByName("test")).thenReturn(target);
        // then
        Assertions.assertEquals(pService.getPerformanceById(1), target);
        Assertions.assertNull(pService.getPerformanceById(999));
        Assertions.assertEquals(pService.getPerformanceByName("test"), target);
    }


    @Test
    public void deletePerformance_공연_삭제_성공() {
        // given
        int delParam = 1;
        // when
        when(pMapper.deletePerformance(delParam)).thenReturn(1);
        // then
        Assertions.assertDoesNotThrow(() -> {
            pService.deletePerformance(delParam);
        });
    }

    @Test
    @DisplayName("mapper 수행 결과 0건 일때 runtime exception 발생시킨다")
    public void deletePerformance_공연_삭제_실패() {
        // given
        int delParam = 1;
        // when
        when(pMapper.deletePerformance(delParam)).thenReturn(0);
        // then
        Assertions.assertThrows(CustomRuntimeException.class, () -> {
            pService.deletePerformance(delParam);
        });
    }

    /*
    공연 수정 성공
    수정/삭제전 추첨 진행 된 상태면 사용자 알림
    수정/삭제전 결제 진행 여부 체크
     */

}
