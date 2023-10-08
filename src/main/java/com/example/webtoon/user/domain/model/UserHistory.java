package com.example.webtoon.user.domain.model;

import com.example.webtoon.global.domain.BaseEntity;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private WebtoonChapter webtoonChapter;
}
