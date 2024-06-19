package com.travel.jeju.model.common;

import lombok.Data;

@Data
public class BaseErrorModel {

    private String code;

    private String message;

    private String desc;

}
