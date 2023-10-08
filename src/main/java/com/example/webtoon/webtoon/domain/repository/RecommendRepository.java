package com.example.webtoon.webtoon.domain.repository;

import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.webtoon.domain.model.Comment;
import com.example.webtoon.webtoon.domain.model.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend,Long> {
    Integer countByCommentAndRecommendTypeIsTrue(Comment comment);
    Integer countByCommentAndRecommendTypeIsFalse(Comment comment);
    boolean existsByCommentAndUser(Comment comment, User user);
    Recommend findByCommentAndUser(Comment comment, User user);
}
