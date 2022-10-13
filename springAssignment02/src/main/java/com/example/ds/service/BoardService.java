package com.example.ds.service;

import com.example.ds.dto.BoardListResponseDto;
import com.example.ds.dto.BoardRequestDto;
import com.example.ds.dto.BoardResponseDto;
import com.example.ds.entity.Board;
import com.example.ds.entity.Comment;
import com.example.ds.entity.User;
import com.example.ds.exception.RequestException;
import com.example.ds.jwt.JwtTokenProvider;
import com.example.ds.repository.BoardRepository;
import com.example.ds.repository.CommentRepository;
import com.example.ds.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 게시글 등록
    @Transactional
    public BoardResponseDto registerBoard(BoardRequestDto requestDto, HttpServletRequest request){
        String accessToken = request.getHeader("ACCESS_TOKEN");
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);

        String username = (String)accessClaims.get("username");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RequestException());

        Board board = new Board(requestDto,user);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    // 게시글 목록 조회
    @Transactional(readOnly=true)
    public List<BoardListResponseDto> getBoardList(){
        List<Board> board = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardListResponseDto> boardDtoList = board.stream().map(BoardListResponseDto::new).collect(Collectors.toList());
        return boardDtoList;
    }

    // 특정 게시글 조회
    @Transactional(readOnly=true)
    public BoardResponseDto getBoardId(Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException()
        );
        return new BoardResponseDto(board);
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoardId(BoardRequestDto requestDto, Long id, HttpServletRequest request){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException()
        );
        String accessToken = request.getHeader("ACCESS_TOKEN");
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        String username = (String)accessClaims.get("username");
        if(!username.equals(board.getUser().getUsername())){
            throw new RequestException();
        }
        board.updatingBoard(requestDto);
        return new BoardResponseDto(board);
    }

    // 게시글 삭제
    @Transactional
    public String deleteBoardId(Long id, HttpServletRequest request){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException()
        );
        String accessToken = request.getHeader("ACCESS_TOKEN");
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        String username = (String)accessClaims.get("username");
        if(!username.equals(board.getUser().getUsername())){
            throw new RequestException();
        }
        List <Comment> commentList =  commentRepository.findByBoardId(id);
        commentRepository.deleteAll(commentList);
        boardRepository.deleteById(id);
        return "해당 게시글과 댓글 모두 삭제 완료";
    }

}
