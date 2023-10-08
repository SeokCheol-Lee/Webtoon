package com.example.webtoon.webtoon.domain.dto;

import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WebtoonChapterInfo {
    private Long id;
    private String webtoonName;
    private String chapterName;
    private Integer chapterCount;
    private List<String> webtoonImgs;

    public static WebtoonChapterInfo of(WebtoonChapter webtoonChapter){
        return WebtoonChapterInfo.builder()
            .id(webtoonChapter.getId())
            .webtoonName(webtoonChapter.getWebtoon().getWebtoonName())
            .chapterName(webtoonChapter.getChapterName())
            .chapterCount(webtoonChapter.getChapterCount())
            .webtoonImgs(webtoonChapter.getWebtoonImgAsStringList())
            .build();
    }
}
