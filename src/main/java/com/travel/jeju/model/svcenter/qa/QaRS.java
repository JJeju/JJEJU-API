package com.travel.jeju.model.svcenter.qa;

import java.util.List;

import com.travel.jeju.dto.ComplaintDto;

import lombok.Data;

@Data
public class QaRS {

    private List<ComplaintDto> complaint;
    
}
