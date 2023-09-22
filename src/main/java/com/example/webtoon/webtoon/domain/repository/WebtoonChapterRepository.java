package com.example.webtoon.webtoon.domain.repository;

import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonChapterRepository extends JpaRepository<WebtoonChapter,Long> {
    Integer countByWebtoon(Webtoon webtoon);
}
