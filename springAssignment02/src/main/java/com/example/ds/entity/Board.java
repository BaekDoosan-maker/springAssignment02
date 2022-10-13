package com.example.ds.entity;

import com.example.ds.dto.BoardRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "board")
public class Board extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "boardId")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "board") //
    List<Comment> comments = new ArrayList<>();

    // 게시글 생성 시
    public Board(BoardRequestDto requestDto, User user){
        this.title = requestDto.getTitle();  // 제목
        this.content = requestDto.getContent();  // 내용
        this.user = user;  // 유저
    }

    // 게시글 수정
    public void updatingBoard(BoardRequestDto requestDto){
        this.title = requestDto.getTitle(); // 제목
        this.content = requestDto.getContent(); // 내용
    }
}
