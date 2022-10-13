package com.example.ds.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="users") // users 테이블
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userId")
    private Long id;

    @Column(name = "username", nullable = false, length= 12, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password",nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Board> board = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Comment> comments = new ArrayList<>();

}
