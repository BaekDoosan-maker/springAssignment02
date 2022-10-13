package com.example.ds.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Auth {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String accessToken; // 엑세스 토큰
    private String refreshToken; // 리프레시 토큰

    @ManyToOne // 다대일 (N :1 )
    @JoinColumn(name = "userId")  // JoinColumn 어노테이션은 외래 키를 매핑할때 사용
    private User user;

    public void accessUpdate(String accessToken) {
        this.accessToken = accessToken;
    }

    public void refreshUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
