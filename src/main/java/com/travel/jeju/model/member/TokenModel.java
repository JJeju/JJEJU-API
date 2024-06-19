package com.travel.jeju.model.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TokenModel {

    private String grantType;

    private String accessToken;

    private String refreshToken;
}
