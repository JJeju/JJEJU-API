package com.travel.jeju.model.main.home;

import java.util.List;

import com.travel.jeju.dto.ComplaintDto;

import lombok.Data;

@Data
public class HomeComplaintRS {

    private List<ComplaintDto> complaint;
    
}
