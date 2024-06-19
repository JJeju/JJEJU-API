package com.travel.jeju.constant;

import lombok.Getter;

@Getter
public enum FileConstant {

    KB("KB")
    
    ;


    private String value;

    private FileConstant(String value){
        this.value = value;
    }


}
