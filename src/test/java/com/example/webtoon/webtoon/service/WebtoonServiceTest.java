package com.example.webtoon.webtoon.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.webtoon.domain.model.Webtoon;
import com.example.webtoon.webtoon.domain.repository.WebtoonRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {

    @InjectMocks
    private WebtoonService webtoonService;
    @Mock
    private WebtoonRepository webtoonRepository;

    private final User user = User.builder()
        .id(1L)
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .build();

    private final Webtoon webtoon = Webtoon.builder()
        .author(user)
        .webtoonName("test")
        .build();

    @Test
    void getWebtoon() {
        //given
        given(webtoonRepository.findById(any()))
            .willReturn(Optional.ofNullable(webtoon));
        //when
        Webtoon serviceWebtoon = webtoonService.getWebtoon(any());
        //then
        assertEquals(user, serviceWebtoon.getAuthor());
        assertEquals("test",serviceWebtoon.getWebtoonName());
    }

    @DisplayName("웹툰 검색 실패 - 해당하는 웹툰이 없음")
    @Test
    void getWebtoon_NotFoundWebtoon() {
        //given
        given(webtoonRepository.findById(any()))
            .willReturn(Optional.empty());
        //when
        GlobalException exception = assertThrows(GlobalException.class,
            () -> webtoonService.getWebtoon(3L));
        //then
        assertEquals(ErrorCode.NOT_FOUND_WEBTOON, exception.getErrorCode());
    }
}