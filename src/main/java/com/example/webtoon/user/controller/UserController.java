package com.example.webtoon.user.controller;

import com.example.webtoon.user.domain.dto.InterestWebtoonDto;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.service.InterestWebtoonService;
import com.example.webtoon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final InterestWebtoonService interestWebtoonService;

    @PostMapping("/regist-author")
    public ResponseEntity<String> RegisterAuthor(@AuthenticationPrincipal UserDetails user){
        userService.registerAuthor(user.getUsername());
        return ResponseEntity.ok("작가 추가 성공");
    }

    @GetMapping("/interestWebtoon")
    public ResponseEntity<List<InterestWebtoonDto>> getInterestWebtoon(Long userId){
        List<InterestWebtoonDto> interestWebtoons = interestWebtoonService.getInterestWebtoons(userId);
        return ResponseEntity.ok(interestWebtoons);
    }

    @PostMapping("interestWebtoon")
    public ResponseEntity<String> createInterestWebtoon(Long userId, Long webtoonId){
        this.interestWebtoonService.createInterestWebtoon(userId,webtoonId);
        return ResponseEntity.ok("Success");
    }
}
