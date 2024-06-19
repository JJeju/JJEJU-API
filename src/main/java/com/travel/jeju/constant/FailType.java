package com.travel.jeju.constant;

import lombok.Getter;

@Getter
public enum FailType {


    USER_NOT_FOUNT("User Not Found"),
	ACOUNT_DISABLE("Acount is Disabled"),
	ACOUNT_EXPRIED("Acount is Expired"),
	ACOUNT_LOCK("Acount is Lock"),

    PASS_NOT_MATCH("PassWord Not Match"),

    ;

    private String value;

    private FailType (String value){
        this.value = value;
    }

    public static FailType fromMessage(String value) {

        for (FailType errorCode : FailType.values()) {
            if (errorCode.getValue().equals(value)) {
                return errorCode;
            }
        }
    
        return null;
    }
}
