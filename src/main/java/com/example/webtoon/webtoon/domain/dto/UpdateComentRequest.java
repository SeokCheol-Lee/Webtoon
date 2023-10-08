package com.example.webtoon.webtoon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UpdateComentRequest {
    private Long commentId;
    private Long userId;
    private String content;
}
