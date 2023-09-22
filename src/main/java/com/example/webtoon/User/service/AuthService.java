package com.example.webtoon.User.service;

import com.example.webtoon.User.domain.dto.SignInForm;
import com.example.webtoon.User.domain.dto.SignUpForm;
import com.example.webtoon.User.domain.model.User;
import com.example.webtoon.User.domain.repository.UserRepository;
import com.example.webtoon.exception.ErrorCode;
import com.example.webtoon.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(SignUpForm signUpForm) {
        boolean exists = this.userRepository.existsByEmail(signUpForm.getEmail());
        if (exists) {
            throw new GlobalException(ErrorCode.ALREADY_REGISTER_USER);
        }
        signUpForm.setPassword(this.passwordEncoder.encode(signUpForm.getPassword()));
        this.userRepository.save(User.from(signUpForm));
    }

    public User authenticate(SignInForm signInForm) {
        User user = this.userRepository.findUserByEmail(signInForm.getEmail())
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        if (user.isDeleted()) {
            throw new GlobalException(ErrorCode.DELETED_ACCOUNT);
        }
        if (!passwordEncoder.matches(signInForm.getPassword(), user.getPassword())) {
            throw new GlobalException(ErrorCode.NO_MATCH_PASSWORD);
        }
        return user;
    }
}
