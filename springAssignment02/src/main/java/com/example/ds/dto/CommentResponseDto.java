package com.example.ds.dto;

import com.example.ds.entity.Comment;
import com.example.ds.entity.Timestamped;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass //Timestamped
@EntityListeners(AuditingEntityListener.class) //Timestamped
public class CommentResponseDto extends Timestamped { // 타임스탬프
    private Long id; // id
    private  String content; // 댓글내용
    private Long userId; // 댓글 사용자 id
    private String username; // 댓글 작성자명
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.username =comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}