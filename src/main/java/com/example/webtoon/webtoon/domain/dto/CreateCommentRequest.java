package com.example.webtoon.webtoon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateCommentRequest {
    private Long userId;
    private Long webtoonChapterId;
    private Long commentId;
    private String content;
}
