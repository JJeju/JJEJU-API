package com.travel.jeju.dto;

import lombok.Data;

@Data
public class TokenBlackDto {

    private String username;

    private String refresh_token;
}
