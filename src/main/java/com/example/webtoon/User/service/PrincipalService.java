package com.example.webtoon.User.service;

import com.example.webtoon.User.domain.PrincipalDetails;
import com.example.webtoon.User.domain.model.User;
import com.example.webtoon.User.domain.repository.UserRepository;
import com.example.webtoon.exception.ErrorCode;
import com.example.webtoon.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(useremail)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));
        return new PrincipalDetails(user);
    }
}
