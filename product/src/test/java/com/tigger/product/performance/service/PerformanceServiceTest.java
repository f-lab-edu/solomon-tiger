package com.tigger.product.performance.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.tigger.product.performance.dao.PerformanceMapper;
import com.tigger.product.performance.enums.PerformanceState;
import com.tigger.product.performance.exception.DupPerformanceException;
import com.tigger.product.performance.dto.PerformanceDTO;
import com.tigger.product.performance.exception.CustomRuntimeException;
import com.tigger.product.performance.exception.NonExistentPerformanceException;
import java.util.ArrayList;
import java.util.List;
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
    PerformanceMapper pMapper;
    @InjectMocks
    PerformanceService pService;

    /*
        공연 등록
        - 등록 실행시, 중복 공연 있으면 예외 발생
        - 등록 실행시, 중복 공연 없으면 예외 발생시키지 않음
        - 등록 실행시, 중복되는 일지/장소 있으면 예외 발생
        - 등록 완료 건수 > 0 이면 성공
        - 등록 완료 건수 == 0 이면 실패
     */

    @Test
    @DisplayName("공연 등록 예외 (중복 공연 체크) : null 이 아닌 업체코드와 업체상품코드로 중복공연 체크한 카운트가 0보다 크면, DupPerformanceException 발생시킨다")
    public void addDupPerformance() {
        PerformanceDTO dto = this.generatePerformance();

        when(this.pMapper.getDupPerformanceCnt(dto.getEnterpriseCode(), dto.getEnterprisePid())).thenReturn(1);

        assertThrows(DupPerformanceException.class, () -> {
            this.pService.chkDupPerformance(dto);
        });

    }

    @Test
    @DisplayName("공연 등록/수정 정상 : null 이 아닌 업체코드와 업체상품코드로 중복공연 체크한 카운트가 0이면 예외를 발생시키지 않는다")
    public void addValidatedPerformance() {
        PerformanceDTO dto = this.generatePerformance();

        when(this.pMapper.getDupPerformanceCnt(dto.getEnterpriseCode(), dto.getEnterprisePid())).thenReturn(0);

        assertDoesNotThrow(() -> {
            this.pService.chkDupPerformance(dto);
        });
    }

    @Test
    public void insertPerformance_공연_등록_성공() {
        PerformanceDTO newDto = this.generatePerformance();

        when(this.pMapper.insertPerformance(any())).thenReturn(1);

        assertDoesNotThrow(() -> {
            this.pService.registerPerformance(newDto);
        });
    }

    @Test
    @DisplayName("insert mapper 수행 결과 0건 일때 runtime exception 발생시킨다")
    public void insertPerformance_공연_등록_실패() {
        PerformanceDTO newDto = this.generatePerformance();

        when(this.pMapper.insertPerformance(any())).thenReturn(0);

        assertThrows(CustomRuntimeException.class, () -> {
            this.pService.registerPerformance(newDto);
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

        when(this.pMapper.getById(1)).thenReturn(target);

        assertEquals(this.pService.getPerformanceById(1), target);
    }

    @Test
    @DisplayName("존재하지 않는 공연 ID로 조회 요청시 NonExistentPerformanceException 던지며 조회 실패")
    public void getPerformanceById_공연_조회_실패() {
        PerformanceDTO targetInfo = this.generatePerformance();

        when(this.pMapper.getById(999)).thenReturn(null);

        assertThrows(NonExistentPerformanceException.class, () -> {
            this.pService.getPerformanceById(999);
        });
    }

    @Test
    @DisplayName("공연명 검색해서 공연 정보 조회")
    public void getPerformanceByName_공연_조회_성공() {
        List<PerformanceDTO> list = new ArrayList<>();
        list.add(this.generatePerformance());

        when(this.pMapper.getListByName("test")).thenReturn(list);

        assertEquals(this.getPerformanceListByName("test").size(), 1);
    }

    @Test
    @DisplayName("공연명 조회 결과 없을 때 null 리턴")
    public void getPerformanceByName_공연_조회_결과없을때() {
        PerformanceDTO target = this.generatePerformance();

        when(this.pMapper.getListByName(anyString())).thenReturn(null);

        assertNull(this.getPerformanceListByName("NonExistentPerformance"));
    }

    private List<PerformanceDTO> getPerformanceListByName(String name) {
        return this.pMapper.getListByName(name);
    }

    /*
        공연 수정(id 기준)
        - 공연 상태값 변경시(정상 -> 취소), 결제 건수 있으면 예외 발생
        - 공연 상태값 변경시(취소 -> 정상), 예외 발생
        - 공연 특정 타임 상태값 변경시(정상 -> 취소), 결제 건수 있으면 예외 발생
        - 공연 상태값을 제외한 타 공연 정보 필드 변경시 validation 체크 없이 수정 성공
        - 공연 상태값 변경이 제대로 이루어지지 않은 경우
     */

    @Test
    @DisplayName("공연 상태값 변경시(정상 에서 -> 취소 로), 결제 건수 있으면 예외 발생시킨다")
    public void updatePerformanceFlag_결제_존재() {
        when(this.pMapper.getCurrentState(anyInt())).thenReturn(PerformanceState.NORMAL.getValue());
        when(this.pMapper.chkExistPaymentCnt(anyInt())).thenReturn(1);

        assertThrows(CustomRuntimeException.class, () -> {
            this.pService.updateState(1);
        });
    }

    @Test
    @DisplayName("취소 상태인 공연의 상태값 변경시, 예외 발생시킨다")
    public void updatePerformanceFlag_CancelToNormal() {
        when(this.pMapper.getCurrentState(anyInt())).thenReturn(PerformanceState.CANCEL.getValue());

        assertThrows(CustomRuntimeException.class, () -> {
            this.pService.updateState(1);
        });
    }

    private PerformanceDTO generatePerformance() {
        PerformanceDTO newPerformanceDTO = PerformanceDTO.builder()
            .no(1)
            .name("test")
            .categoryCode(101)
            .enterpriseCode(11)
            .enterprisePid("a11")
            .build();
        return newPerformanceDTO;
    }

    @Test
    @DisplayName("updateState 결과 validation 체크")
    public void validResultAfterUpdateState() {
        when(this.pMapper.getCurrentState(anyInt())).thenReturn(PerformanceState.NORMAL.getValue());
        assertThrows(CustomRuntimeException.class, () -> {
            this.pService.updateState(1);
        });
    }

}
