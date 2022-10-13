package com.example.ds.controller;

import com.example.ds.dto.BoardListResponseDto;
import com.example.ds.dto.BoardResponseDto;
import com.example.ds.service.BoardService;
import com.example.ds.dto.BoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성
    @PostMapping("/auth/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request){
        return boardService.registerBoard(requestDto,request);
    }

    // 게시글 목록 조회
    @GetMapping("/boards")
    public List<BoardListResponseDto> getBoardList(){
        return boardService.getBoardList();
    }

    // 특정 게시글 조회
    @GetMapping("/boards/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id){
        return boardService.getBoardId(id);
    }

    // 게시글 수정
    @PutMapping("/auth/boards/{id}")
    public BoardResponseDto updateBoard(@RequestBody BoardRequestDto requestDto, @PathVariable Long id, HttpServletRequest request){
        return boardService.updateBoardId(requestDto,id,request);
    }

    // 게시글 삭제
    @DeleteMapping ("/auth/boards/{id}")
    public String deleteBoard(@PathVariable Long id, HttpServletRequest request){
        return boardService.deleteBoardId(id, request);
    }
}
