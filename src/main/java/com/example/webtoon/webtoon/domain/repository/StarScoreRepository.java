package com.example.webtoon.webtoon.domain.repository;

import com.example.webtoon.global.domain.dto.WebtoonStarScore;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.webtoon.domain.model.StarScore;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StarScoreRepository extends JpaRepository<StarScore, Long> {
    boolean existsByUserAndWebtoonChapter(User user, WebtoonChapter webtoonChapter);
    Optional<StarScore> findByUserAndWebtoonChapter(User user, WebtoonChapter webtoonChapter);

    @Query(" SELECT new com.example.webtoon.global.domain.dto.WebtoonStarScore(wc.webtoon.id, avg(s.starscore)) "
        + " from WebtoonChapter wc inner join StarScore s on wc.id = s.webtoonChapter.id "
        + " group by wc.webtoon.id")
    List<WebtoonStarScore> findByWebtoon();
}
