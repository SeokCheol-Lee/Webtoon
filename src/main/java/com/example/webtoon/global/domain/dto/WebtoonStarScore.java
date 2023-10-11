package com.example.webtoon.global.domain.dto;

import lombok.Getter;

@Getter
public class WebtoonStarScore {

    private Long webtoonId;
    private Double starScore;

    public WebtoonStarScore(Long webtoonId, Double starScore){
        this.webtoonId = webtoonId;
        this.starScore = starScore;
    }

}
