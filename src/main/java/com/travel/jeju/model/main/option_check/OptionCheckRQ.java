package com.travel.jeju.model.main.option_check;

import java.util.List;

import lombok.Data;

@Data
public class OptionCheckRQ {

    private List<String> optionList;

    private String category;
}
