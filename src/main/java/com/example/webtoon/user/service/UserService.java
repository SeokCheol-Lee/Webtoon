package com.example.webtoon.user.service;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User registerAuthor(String email){
        User user = userRepository.findUserByEmail(email)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        user.changeRole();
        return user;
    }

}
