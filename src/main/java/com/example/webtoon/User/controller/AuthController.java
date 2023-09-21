package com.example.webtoon.User.controller;

import com.example.webtoon.User.domain.dto.SignInForm;
import com.example.webtoon.User.domain.dto.SignUpForm;
import com.example.webtoon.User.domain.model.User;
import com.example.webtoon.User.security.TokenProvider;
import com.example.webtoon.User.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody SignUpForm signUpForm){
        authService.register(signUpForm);
        return ResponseEntity.ok("회원가입 성공");
    }
    @PostMapping("/signin")
    private ResponseEntity<String> SignIn(@RequestBody SignInForm signInForm){
        User user = this.authService.authenticate(signInForm);
        String token = this.tokenProvider.generateToken(user.getEmail(),user.getRole());
        return ResponseEntity.ok(token);
    }
}
