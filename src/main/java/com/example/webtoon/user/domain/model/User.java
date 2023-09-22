package com.example.webtoon.user.domain.model;

import com.example.webtoon.global.domain.BaseEntity;
import com.example.webtoon.user.domain.dto.SignUpForm;
import com.example.webtoon.user.type.Authority;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Authority role;
    private boolean isDeleted;

    public void changeRole(){
        this.role = Authority.ROLE_AUTHOR;
    }

    public static User from(SignUpForm form) {
        return User.builder()
            .nickname(form.getNickname())
            .email(form.getEmail())
            .password(form.getPassword())
            .role(Authority.ROLE_USER)
            .isDeleted(false)
            .build();
    }
}
