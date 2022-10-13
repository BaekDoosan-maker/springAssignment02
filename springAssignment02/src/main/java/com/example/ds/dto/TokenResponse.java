package com.example.ds.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private String ACCESS_TOKEN; // 엑세스 토큰
    private String REFRESH_TOKEN; // 리프레시 토큰
}
