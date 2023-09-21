package com.example.webtoon.User.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpForm {
    private String nickname;
    private String email;
    private String password;
}
