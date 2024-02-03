package com.example.webtoon.user.controller;

import com.example.webtoon.user.domain.dto.SignInForm;
import com.example.webtoon.user.domain.dto.SignUpForm;
import com.example.webtoon.user.domain.dto.TokenResponse;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.security.TokenProvider;
import com.example.webtoon.user.service.AuthService;
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
    private ResponseEntity<TokenResponse> SignIn(@RequestBody SignInForm signInForm){
        User user = this.authService.authenticate(signInForm);
        String token = this.tokenProvider.generateToken(user.getEmail(),user.getRole());
        TokenResponse tokenResponse = new TokenResponse(token);
        return ResponseEntity.ok(tokenResponse);
    }
}
