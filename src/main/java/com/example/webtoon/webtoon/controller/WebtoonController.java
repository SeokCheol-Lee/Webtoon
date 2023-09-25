package com.example.webtoon.webtoon.controller;

import com.example.webtoon.webtoon.domain.dto.WebtoonDto;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.service.WebtoonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/webtoon")
@RestController
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;

    @Transactional
    @GetMapping("/search")
    public ResponseEntity<WebtoonDto> searchWebtoon(@RequestParam Long id){
        Webtoon webtoon = webtoonService.getWebtoon(id);
        return ResponseEntity.ok(WebtoonDto.from(webtoon));
    }
}
