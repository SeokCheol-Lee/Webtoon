package com.example.webtoon.user.domain.dto;

import com.example.webtoon.user.domain.model.InterestWebtoon;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterestWebtoonDto {
    private Long webtoonId;
    private String webtoonName;

    public static InterestWebtoonDto of(InterestWebtoon interestWebtoon){
        return InterestWebtoonDto.builder()
                .webtoonId(interestWebtoon.getWebtoon().getId())
                .webtoonName(interestWebtoon.getWebtoon().getWebtoonName())
                .build();
    }
}
