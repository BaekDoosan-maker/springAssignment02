package com.example.ds.dto;

import com.example.ds.entity.Board;
import com.example.ds.entity.Timestamped;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@MappedSuperclass //Timestamped
@EntityListeners(AuditingEntityListener.class) //Timestamped
public class BoardResponseDto extends Timestamped {
    private Long id;
    private String title;
    private  String content;
    private String username;
    private Long userId;

    private List<CommentResponseDto> comments;
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUser().getUsername();
        this.userId = board.getUser().getId();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.comments = board.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
