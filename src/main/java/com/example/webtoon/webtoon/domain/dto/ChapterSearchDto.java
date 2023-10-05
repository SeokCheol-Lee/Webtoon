package com.example.webtoon.webtoon.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterSearchDto {
    private int page;             // 현재 페이지 번호
    private int recordSize;       // 페이지당 출력할 데이터 개수
    private int pageSize;         // 화면 하단에 출력할 페이지 사이즈

    public ChapterSearchDto() {
        this.page = 1;
        this.recordSize = 10;
        this.pageSize = 10;
    }
}
