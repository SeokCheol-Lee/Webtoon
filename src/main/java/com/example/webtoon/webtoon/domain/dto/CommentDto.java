package com.example.webtoon.webtoon.domain.dto;

import com.example.webtoon.webtoon.domain.model.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
    private Long id;
    private String nickname;
    private String content;
    private Integer recommend;
    private Integer notRecommend;
    private LocalDateTime dateTime;

    public static CommentDto of(Comment comment, Integer recommend, Integer notRecommend){
        return CommentDto.builder()
            .id(comment.getId())
            .nickname(comment.getWriter().getNickname())
            .content(comment.getContent())
            .recommend(recommend)
            .notRecommend(notRecommend)
            .dateTime(comment.getModifiedAt())
            .build();
    }
}
