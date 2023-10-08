package com.example.webtoon.webtoon.domain.repository;

import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.webtoon.domain.model.StarScore;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarScpreRepository extends JpaRepository<StarScore, Long> {
    boolean existsByUserAndWebtoonChapter(User user, WebtoonChapter webtoonChapter);
    Optional<StarScore> findByUserAndWebtoonChapter(User user, WebtoonChapter webtoonChapter);
}
