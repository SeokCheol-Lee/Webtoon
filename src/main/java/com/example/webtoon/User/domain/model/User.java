package com.example.webtoon.User.domain.model;

import com.example.webtoon.User.domain.dto.SignUpForm;
import com.example.webtoon.User.type.Authority;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oauth;
    private String oauthId;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;
    private String password;
    private Authority role;
    private boolean isDeleted;

    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    private boolean verify;

    public static User from(SignUpForm form){
        return User.builder()
            .nickname(form.getNickname())
            .email(form.getEmail())
            .password(form.getPassword())
            .verify(false)
            .build();
    }
}
