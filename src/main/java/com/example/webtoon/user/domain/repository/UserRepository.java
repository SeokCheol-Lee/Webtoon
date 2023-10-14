package com.example.webtoon.user.domain.repository;

import com.example.webtoon.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("delete from User u where u.isDeleted = true")
    void deleteAllByIdInQuery();
}
