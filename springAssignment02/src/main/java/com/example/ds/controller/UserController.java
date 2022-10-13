package com.example.ds.controller;


import com.example.ds.dto.UserRequestDto;
import com.example.ds.service.UserService;
import com.example.ds.dto.UserResponseDto;
import com.example.ds.dto.UserTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    // 회원가입
    @PostMapping("/signup")
    public UserResponseDto signup(@RequestBody UserRequestDto userRequestDto) {
        return userService.register(userRequestDto);
    }

    // 로그인
    @PostMapping("/login")
    public UserTokenResponseDto login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) throws Exception {
        return userService.doLogin(userRequestDto,response);
    }

    //Access Token을 재발급 위한 api
    @PostMapping("/issue")
    public ResponseEntity issueAccessToken(HttpServletRequest request) throws Exception {
        return ResponseEntity
                .ok()
                .body(userService.issueAccessToken(request));
    }
}
