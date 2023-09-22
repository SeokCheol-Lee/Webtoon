package com.example.webtoon.webtoon.controller;

import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.webtoon.domain.dto.CreateWebtoonRequest;
import com.example.webtoon.webtoon.domain.dto.UploadWebtoonRequest;
import com.example.webtoon.webtoon.domain.dto.WebtoonDto;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.service.WebtoonManageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/webtoon-manage")
@PreAuthorize("hasRole('AUTHOR')")
@RestController
@RequiredArgsConstructor
public class WebtoonManageController {

    private final WebtoonManageService webtoonManageService;

    @PostMapping("/create")
    public ResponseEntity<WebtoonDto> createWebtoon(@RequestBody CreateWebtoonRequest request,
        @AuthenticationPrincipal UserDetails user){
        Webtoon webtoon = webtoonManageService.createWebtoon(request, user.getUsername());
        return ResponseEntity.ok(WebtoonDto.from(webtoon));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadWebtoon(@RequestBody UploadWebtoonRequest request,
        List<MultipartFile> imgs, @AuthenticationPrincipal UserDetails user){
        this.webtoonManageService.uploadWebtoonChapter(user.getUsername(),request,imgs);
        return ResponseEntity.ok("업로드 성공");
    }
}
