package com.example.ds.entity;

import com.example.ds.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "commentId")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // 댓글 생성 시
    public Comment(CommentRequestDto requestDto, Board board, User user){
        this.content = requestDto.getContent();
        this.board = board;
        this.user = user;
    }

    // 댓글 수정
    public void updateComment(CommentRequestDto requestDto){
        this.content = requestDto.getContent();
    }
}
