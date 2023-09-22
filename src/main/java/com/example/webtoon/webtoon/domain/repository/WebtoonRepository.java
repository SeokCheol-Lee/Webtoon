package com.example.webtoon.webtoon.domain.repository;

import com.example.webtoon.webtoon.domain.model.Webtoon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonRepository extends JpaRepository<Webtoon,Long> {
    Optional<Webtoon> findByWebtoonName(String name);
}
