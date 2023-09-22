package com.example.webtoon.webtoon.domain.model;

import com.example.webtoon.global.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WebtoonImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgUrl;

    private WebtoonImg(String url){
        this.imgUrl = url;
    }

    public static WebtoonImg of(String url){
        return new WebtoonImg(url);
    }
}
