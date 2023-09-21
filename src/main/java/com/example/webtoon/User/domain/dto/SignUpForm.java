package com.example.webtoon.User.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {
    private String nickname;
    private String email;
    private String password;
}
