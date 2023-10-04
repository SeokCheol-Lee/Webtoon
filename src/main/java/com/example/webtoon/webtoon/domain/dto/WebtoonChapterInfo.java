package com.example.webtoon.webtoon.domain.dto;

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
}
