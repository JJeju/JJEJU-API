package com.travel.jeju.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductDto {

    private Integer idx; // 상품 PK

    private Integer p_fk_cnum; // 상품업체 일련번호

    private String p_name; // 상품 명

    private Integer p_price; // 상품 비용

    private String p_content; // 상품 설명

    private Boolean p_reserve_status; // 예약상품인지 여부

    private Boolean p_exposure; // 상품 노출여부

    private Integer file_group_no; // 상품관련 파일 그룹번호

    private LocalDateTime create_dt; // 생성일시
    
    private LocalDateTime update_dt; // 수정일시

}
