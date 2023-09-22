package com.example.webtoon.webtoon.service;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WebtoonService {
    private final WebtoonRepository webtoonRepository;
    public Webtoon getWebtoon(String webtoonName){
        return this.webtoonRepository.findByWebtoonName(webtoonName)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_WEBTOON));
    }
}
