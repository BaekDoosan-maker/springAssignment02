package com.example.ds.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotNull
    private String username; // 유저명

    @NotNull
    private String password;  // 유저 비밀번호

    @NotNull
    private String passwordCheck;  // 유저 비밀번호 확인


}
