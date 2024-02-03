package com.example.webtoon.webtoon.domain.dto;

import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.tyoe.Day;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class WebtoonDto {
    private Long id;
    private String author;
    private String webtoonName;
    private List<String> hashtags;
    private Day uploadDay;
    private Long viewCount;
    private Double starScore;
    private LocalDateTime createdAt;

    public static WebtoonDto from(Webtoon webtoon){
        return WebtoonDto.builder()
            .id(webtoon.getId())
            .author(webtoon.getAuthor().getNickname())
            .webtoonName(webtoon.getWebtoonName())
            .hashtags(webtoon.getHashtagsAsStringList())
            .uploadDay(webtoon.getUploadDay())
            .viewCount(webtoon.getViewCount())
            .starScore(webtoon.getStarScore())
            .createdAt(webtoon.getCreatedAt())
            .build();
    }
}
