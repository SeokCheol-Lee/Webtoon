package com.example.webtoon.webtoon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UploadWebtoonRequest {
    private Long id;
    private String webtoonName;
    private String chapterName;
}
