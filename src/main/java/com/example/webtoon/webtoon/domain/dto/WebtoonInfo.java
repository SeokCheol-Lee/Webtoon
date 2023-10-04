package com.example.webtoon.webtoon.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WebtoonInfo {
    private Long id;
    private String author;
    private List<String> hashtags;
    private String uploadDay;
    private Long viewCount;
    private Double starScore;
}
