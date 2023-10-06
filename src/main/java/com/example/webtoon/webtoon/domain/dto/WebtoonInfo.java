package com.example.webtoon.webtoon.domain.dto;

import com.example.webtoon.webtoon.domain.model.Webtoon;
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

    public static WebtoonInfo of(Webtoon webtoon){
        return WebtoonInfo.builder()
            .id(webtoon.getId())
            .author(webtoon.getAuthor().getNickname())
            .hashtags(webtoon.getHashtagsAsStringList())
            .viewCount(webtoon.getViewCount())
            .starScore(webtoon.getStarScore())
            .build();
    }
}
