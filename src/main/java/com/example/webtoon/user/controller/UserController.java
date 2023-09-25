package com.example.webtoon.user.controller;

import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/regist-author")
    public ResponseEntity<String> RegisterAuthor(@AuthenticationPrincipal UserDetails user){
        userService.registerAuthor(user.getUsername());
        return ResponseEntity.ok("작가 추가 성공");
    }
}
