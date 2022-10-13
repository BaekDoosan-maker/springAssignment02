package com.example.ds.dto;

import com.example.ds.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String username; // 유저명
    public UserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
