package com.example.ds.dto;


import com.example.ds.entity.User;
import lombok.Getter;

@Getter
public class UserTokenResponseDto { // 유저 토큰
    private final Long id;
    private final String username; // 유저명
    private final String accessToken; // 엑세스토큰
    private final String refreshToken; // 리프레시 토큰
    public UserTokenResponseDto(User user, String accessToken, String refreshToken){
        this.id = user.getId();
        this.username = user.getUsername();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
