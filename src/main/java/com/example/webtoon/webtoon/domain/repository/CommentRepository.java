package com.example.webtoon.webtoon.domain.repository;

import com.example.webtoon.webtoon.domain.model.Comment;
import com.example.webtoon.webtoon.domain.model.WebtoonChapter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByParentAndIsDeletedIsFalseAndWebtoonChapter(
        Comment comment, WebtoonChapter webtoonChapter);
    List<Comment> findByParentAndIsDeletedFalse(Comment comment);

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);
}
