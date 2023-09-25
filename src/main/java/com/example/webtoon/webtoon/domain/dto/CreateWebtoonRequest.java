package com.example.webtoon.webtoon.domain.dto;

import com.example.webtoon.webtoon.domain.tyoe.Day;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateWebtoonRequest {
    private Long id;
    private String webtoonName;
    private Day uploadDay;
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
}
