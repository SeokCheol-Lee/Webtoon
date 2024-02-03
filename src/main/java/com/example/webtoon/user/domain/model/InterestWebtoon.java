package com.example.webtoon.user.domain.model;

import com.example.webtoon.global.domain.BaseEntity;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestWebtoon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Webtoon webtoon;

    private InterestWebtoon(User user, Webtoon webtoon){
        this.user = user;
        this.webtoon = webtoon;
    }
    public static InterestWebtoon of(User user, Webtoon webtoon){
        return new InterestWebtoon(user,webtoon);
    }
}
