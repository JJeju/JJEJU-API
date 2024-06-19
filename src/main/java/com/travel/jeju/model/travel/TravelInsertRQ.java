package com.travel.jeju.model.travel;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TravelInsertRQ {

    private String tr_title;

    private LocalDate tr_in;

    private LocalDate tr_out;

    private String tr_relationship;

}
