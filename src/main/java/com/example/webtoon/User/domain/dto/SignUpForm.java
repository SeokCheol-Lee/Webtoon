package com.example.webtoon.User.domain.dto;

import lombok.Getter;

@Getter
public class SignUpForm {
    private String nickname;
    private String email;
    private String password;
}
