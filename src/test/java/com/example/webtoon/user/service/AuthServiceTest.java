package com.example.webtoon.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.webtoon.user.domain.dto.SignInForm;
import com.example.webtoon.user.domain.dto.SignUpForm;
import com.example.webtoon.user.domain.model.User;
import com.example.webtoon.user.domain.repository.UserRepository;
import com.example.webtoon.global.exception.ErrorCode;
import com.example.webtoon.global.exception.GlobalException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthService authService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private final User user = User.builder()
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .build();

    @Test
    void register() {
        //given
        SignUpForm signUpForm = SignUpForm.builder()
            .email("test@test.com")
            .password("1234")
            .nickname("test")
            .build();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        given(userRepository.existsByEmail(any()))
            .willReturn(false);
        given(passwordEncoder.encode(anyString()))
            .willReturn("encodedPassword");
        //when
        authService.register(signUpForm);
        //then
        verify(userRepository, times(1))
            .save(captor.capture());
        User value = captor.getValue();
        assertEquals(signUpForm.getEmail(),value.getEmail());
        assertEquals(signUpForm.getNickname(), value.getNickname());
        assertEquals(signUpForm.getPassword(), value.getPassword());
    }

    @DisplayName("회원가입 실패 - 가입한 이메일 존재")
    @Test
    void register_existEmail() {
        //given
        SignUpForm signUpForm = SignUpForm.builder()
            .email("test@test.com")
            .password("1234")
            .nickname("test")
            .build();
        given(userRepository.existsByEmail(any()))
            .willReturn(true);
        //when
        GlobalException exception = assertThrows(GlobalException.class,
        () -> authService.register(signUpForm));
        //then
        assertEquals(ErrorCode.ALREADY_REGISTER_USER, exception.getErrorCode());
    }

    @Test
    void authenticate() {
        //given
        SignInForm signInForm = SignInForm.builder()
            .email("test@test.com")
            .password("1234")
            .build();
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches("1234", "encodePassword"))
            .willReturn(true);
        //when
        User authenticate  = authService.authenticate(signInForm);
        //then
        assert user != null;
        assertEquals(user.getEmail(), authenticate.getEmail());
        assertEquals(user.getNickname(), authenticate.getNickname());
        assertEquals(user.getPassword(), authenticate.getPassword());
    }

    @DisplayName("로그인 실패 - 유저가 업음")
    @Test
    void authenticate_noExistUser() {
        //given
        SignInForm signInForm = SignInForm.builder()
            .email("test@test.com")
            .password("1234")
            .build();
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.empty());
        //when
        GlobalException exception = assertThrows(GlobalException.class,
            () -> authService.authenticate(signInForm));
        //then
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
    }

    @DisplayName("로그인 실패 - 삭제중인 계정")
    @Test
    void authenticate_DeletedAccount() {
        //given
        User user2 = User.builder()
            .nickname("test")
            .email("aa@naver.com")
            .password("encodePassword")
            .isDeleted(true)
            .build();
        SignInForm signInForm = SignInForm.builder()
            .email("test@test.com")
            .password("1234")
            .build();
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user2));
        //when
        GlobalException exception = assertThrows(GlobalException.class,
            () -> authService.authenticate(signInForm));
        //then
        assertEquals(ErrorCode.DELETED_ACCOUNT, exception.getErrorCode());
    }
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    @Test
    void authenticate_NoMatchPassword() {
        //given
        SignInForm signInForm = SignInForm.builder()
            .email("test@test.com")
            .password("1234")
            .build();
        given(userRepository.findUserByEmail(any()))
            .willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches("1234", "encodePassword"))
            .willReturn(false);
        //when
        GlobalException exception = assertThrows(GlobalException.class,
            () -> authService.authenticate(signInForm));
        //then
        assertEquals(ErrorCode.NO_MATCH_PASSWORD, exception.getErrorCode());
    }
}