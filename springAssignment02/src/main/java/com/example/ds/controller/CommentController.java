package com.example.ds.controller;


import com.example.ds.dto.CommentRequestDto;
import com.example.ds.dto.CommentResponseDto;
import com.example.ds.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    //댓글 생성
    @PostMapping("/auth/comments/{id}")
    public CommentResponseDto create(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.createComment(id, requestDto,request);
    }

    // 댓글 목록 조회
    @GetMapping("/comments/{id}")
    public List<CommentResponseDto> getList(@PathVariable Long id){
        return commentService.getCommentList(id);
    }

    // 댓글 수정
    @PutMapping("/auth/comments/{id}")
    public CommentResponseDto update(@RequestBody CommentRequestDto requestDto, @PathVariable Long id, HttpServletRequest request){
        return commentService.updateComment(requestDto,id,request);
    }

    // 댓글 삭제
    @DeleteMapping ("/auth/comments/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request){
        return commentService.deleteComment(id, request);
    }
}
