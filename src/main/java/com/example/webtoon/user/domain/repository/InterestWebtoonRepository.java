package com.example.webtoon.user.domain.repository;

import com.example.webtoon.user.domain.model.InterestWebtoon;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestWebtoonRepository extends JpaRepository<InterestWebtoon, Long> {
    boolean existsByWebtoonAndUser(Webtoon webtoon, User user);
    List<InterestWebtoon> findAllByUser(User user);
}
