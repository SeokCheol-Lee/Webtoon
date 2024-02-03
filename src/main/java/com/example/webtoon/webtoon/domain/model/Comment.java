package com.example.webtoon.webtoon.domain.model;

import com.example.webtoon.global.domain.BaseEntity;
import com.example.webtoon.user.domain.model.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User writer;
    @ManyToOne
    private WebtoonChapter webtoonChapter;
    @ManyToOne
    private Comment parent;
    private String content;
    private Boolean isDeleted;

    public static Comment of(User user, WebtoonChapter webtoonChapter){
        return Comment.builder()
            .writer(user)
            .webtoonChapter(webtoonChapter)
            .isDeleted(false).build();
    }
}
