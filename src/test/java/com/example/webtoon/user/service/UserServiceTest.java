package com.example.webtoon.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.domain.repository.UserRepository;
import com.example.webtoon.user.type.Authority;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private final User user = User.builder()
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .role(Authority.ROLE_USER)
        .build();

    @Test
    void registerAuthor() {
        //given
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user));
        //when
        User author = userService.registerAuthor(user.getEmail());
        //then
        assertEquals(Authority.ROLE_AUTHOR, author.getRole());
    }
}