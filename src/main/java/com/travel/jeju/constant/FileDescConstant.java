package com.travel.jeju.constant;

import lombok.Getter;

@Getter
public enum FileDescConstant {

    BLOG("Blog"),
    BLOG_MAIN("Blog Main"),

    REVIEW("Review"),
    REVIEW_MAIN("Review Main"),

    PRODUCT("Product"),
    PRODUCT_MAIN("Product Main"),

    COMPANY("Company"),
    COMPANY_MAIN("Company Main"),
    
    ;

    private String value;

    private FileDescConstant(String value){
        this.value = value;
    }

}
