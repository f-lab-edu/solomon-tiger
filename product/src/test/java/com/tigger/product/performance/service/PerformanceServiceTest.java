package com.tigger.product.performance.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.tigger.product.performance.dao.PerformanceMapper;
import com.tigger.product.performance.dao.TimeTableMapper;
import com.tigger.product.performance.enums.PerformanceFlag;
import com.tigger.product.performance.exception.DupPerformanceException;
import com.tigger.product.performance.dto.PerformanceDTO;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.DupTimeAndVenueException;
import com.tigger.product.performance.exception.NonExistentPerformanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


/*
    1. @ExtendWith(MockitoExtension.class) : 테스트 클래스가 Mockito 를 사용함을 의미
    2. @Mock : Mock 객체를 생성
 */

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTest {

    @Mock
    PerformanceDTO performanceDTO;
    @Mock
    PerformanceMapper pMapper;
    @InjectMocks
    PerformanceService pService;
    @Mock
    TimeTableMapper tMapper;
    @InjectMocks
    TimeTableService timeService;

    /*
        공연 등록
        - 등록 실행시, 중복 공연 있으면 예외 발생
        - 등록 실행시, 중복 공연 없으면 예외 발생시키지 않음
        - 등록 실행시, 중복되는 일지/장소 있으면 예외 발생
        - 등록 완료 건수 > 0 이면 성공
        - 등록 완료 건수 == 0 이면 실패
     */

    @Test
    @DisplayName("공연 등록 예외1 (중복 공연 체크) : 중복공연 카운트 > 0 일 때, DupPerformanceException 발생시킨다")
    public void addDupPerformance() {
        int enterCode = performanceDTO.getEnterpriseCode();
        String pid = performanceDTO.getEnterprisePid();

        when(pMapper.getDupPerformanceCnt(enterCode, pid)).thenReturn(1);

        assertThrows(DupPerformanceException.class, () -> {
            pService.chkDupPerformance(performanceDTO);
        });

    }

    @Test
    @DisplayName("공연 등록/수정 정상 : 중복공연 체크 카운트가 0 일 때 예외를 발생시키지 않는다")
    public void addValidatedPerformance() {
        int enterCode = performanceDTO.getEnterpriseCode();
        String pid = performanceDTO.getEnterprisePid();

        when(pMapper.getDupPerformanceCnt(enterCode, pid)).thenReturn(0);

        assertDoesNotThrow(() -> {
            pService.chkDupPerformance(performanceDTO);
        });
    }

    @Test
    @DisplayName("공연 등록 예외2 (중복 일시/장소 체크) : 중복 일시/장소 카운트 > 0 일 때, DupTimeAndVenueException 발생시킨다")
    public void addDupTimeAndVenuePerformance() {
        int venueCode = 1;
        String date = "20211121";
        int hour = 17;
        int minute = 30;
        int runningTime = 120;

        when(tMapper.getDupTimeAndVenueCnt(anyInt(), anyString(), any(), any())).thenReturn(1);

        assertThrows(DupTimeAndVenueException.class, () -> {
            timeService.chkDupTimeAndVenue(venueCode, date, hour, minute, runningTime);
        });
    }

    @Test
    public void insertPerformance_공연_등록_성공() {
        PerformanceDTO newDto = this.generatePerformance();

        when(pMapper.insertPerformance(any())).thenReturn(1);

        assertDoesNotThrow(() -> {
            pService.insertPerformance(newDto);
        });
    }

    @Test
    @DisplayName("insert mapper 수행 결과 0건 일때 runtime exception 발생시킨다")
    public void insertPerformance_공연_등록_실패() {
        PerformanceDTO newDto = this.generatePerformance();

        when(pMapper.insertPerformance(any())).thenReturn(0);

        assertThrows(CustomRuntimeException.class, () -> {
            pService.insertPerformance(newDto);
        });
    }

    /*
        공연 조회
        - 공연 ID 로 조회 성공 케이스
        - 존재하지 않는 공연 ID 로 조회시 예외 발생
        - 공연명으로 조회 성공 케이스
        - 존재하지 않는 공연명으로 조회시 null 리턴
     */

    @Test
    @DisplayName("공연 ID 선택해서 공연 정보 조회 성공")
    public void getPerformanceById_공연_조회_성공() {
        PerformanceDTO target = this.generatePerformance();

        when(pMapper.getById(1)).thenReturn(target);

        assertEquals(pService.getPerformanceById(1), target);
    }

    @Test
    @DisplayName("존재하지 않는 공연 ID로 조회 요청시 NonExistentPerformanceException 던지며 조회 실패")
    public void getPerformanceById_공연_조회_실패() {
        PerformanceDTO targetInfo = this.generatePerformance();

        when(pMapper.getById(999)).thenReturn(null);

        assertThrows(NonExistentPerformanceException.class, () -> {
            pService.getPerformanceById(999);
        });
    }

    @Test
    @DisplayName("공연명 검색해서 공연 정보 조회")
    public void getPerformanceByName_공연_조회_성공() {
        PerformanceDTO targetInfo = this.generatePerformance();

        when(pMapper.getByName("test")).thenReturn(targetInfo);

        assertEquals(pService.getPerformanceByName("test"), targetInfo);
    }

    @Test
    @DisplayName("공연명 조회 결과 없을 때 null 리턴")
    public void getPerformanceByName_공연_조회_결과없을때() {
        PerformanceDTO target = this.generatePerformance();

        when(pMapper.getByName(anyString())).thenReturn(null);

        assertNull(pService.getPerformanceByName("NonExistentPerformance"));
    }

    /*
        공연 수정(id 기준)
        - 공연 상태값 변경시(정상 -> 취소), 결제 건수 있으면 예외 발생
        - 공연 상태값 변경시(취소 -> 정상), 예외 발생
        - 공연 특정 타임 상태값 변경시(정상 -> 취소), 결제 건수 있으면 예외 발생
        - 공연 상태값을 제외한 타 공연 정보 필드 변경시 validation 체크 없이 수정 성공
     */

    @Test
    @DisplayName("공연 상태값 변경시(정상 에서 -> 취소 로), 결제 건수 있으면 예외 발생시킨다")
    public void updatePerformanceFlag_결제_존재() {
        when(pMapper.getCurrentFlag(anyInt())).thenReturn(PerformanceFlag.NORMAL.getValue());
        when(pMapper.chkExistPaymentCnt(anyInt())).thenReturn(1);

        assertThrows(CustomRuntimeException.class, () -> {
            pService.updateFlag(1);
        });
    }

    @Test
    @DisplayName("취소 상태인 공연의 상태값 변경시, 예외 발생시킨다")
    public void updatePerformanceFlag_CancelToNormal() {
        when(pMapper.getCurrentFlag(anyInt())).thenReturn(PerformanceFlag.CANCEL.getValue());

        assertThrows(CustomRuntimeException.class, () -> {
            pService.updateFlag(1);
        });
    }

    @Test
    @DisplayName("공연 특정 타임 상태값 변경시(정상 -> 취소), 결제 건수 있으면 예외 발생")
    public void updateTimeFlag_결제_존재() {
        when(tMapper.getCurrentFlag(anyInt())).thenReturn(PerformanceFlag.NORMAL.getValue());
        when(tMapper.chkExistPaymentCnt(anyInt())).thenReturn(1);

        assertThrows(CustomRuntimeException.class, () -> {
            timeService.updateFlag(1);
        });
    }

    public PerformanceDTO generatePerformance() {
        PerformanceDTO newPerformanceDTO = PerformanceDTO.builder()
            .no(1)
            .name("test")
            .build();
        return newPerformanceDTO;
    }

}
