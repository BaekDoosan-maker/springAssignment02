package com.example.ds.service;

import com.example.ds.dto.CommentRequestDto;
import com.example.ds.dto.CommentResponseDto;
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
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException()
        );
        String accessToken = request.getHeader("ACCESS_TOKEN");
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        String username = (String)accessClaims.get("username");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RequestException());
        Comment comment = new Comment(requestDto, board,user);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 댓글 목록 조회
    @Transactional(readOnly=true)
    public List<CommentResponseDto> getCommentList(Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new RequestException()
        );
        List<Comment> comment = commentRepository.findByBoardId(id);
        return comment.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }


    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long id, HttpServletRequest request){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new RequestException()
        );
        String accessToken = request.getHeader("ACCESS_TOKEN");
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        String username = (String)accessClaims.get("username");
        if(!username.equals(comment.getUser().getUsername())){
            throw new RequestException();
        }
        comment.updateComment(requestDto);
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public String deleteComment(Long id, HttpServletRequest request){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new RequestException()
        );
        String accessToken = request.getHeader("ACCESS_TOKEN");
        Claims accessClaims = jwtTokenProvider.getClaimsFormToken(accessToken);
        String username = (String)accessClaims.get("username");
        if(!username.equals(comment.getUser().getUsername())){
            throw new RequestException();
        }
        commentRepository.deleteById(id);
        return "댓글 삭제 완료";
    }

}
