package com.example.webtoon.User.service;

import com.example.webtoon.User.domain.PrincipalDetails;
import com.example.webtoon.User.domain.dto.GoogleUserInfo;
import com.example.webtoon.User.domain.dto.KakaoUserInfo;
import com.example.webtoon.User.domain.dto.NaverUserInfo;
import com.example.webtoon.User.domain.dto.OAuth2UserInfo;
import com.example.webtoon.User.domain.model.User;
import com.example.webtoon.User.domain.repository.UserRepository;
import com.example.webtoon.User.type.Authority;
import com.example.webtoon.exception.ErrorCode;
import com.example.webtoon.exception.GlobalException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("getClientRegistration: "+userRequest.getClientRegistration());
        log.info("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
        log.info("getAttributes: "+ super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo =null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"),
                String.valueOf(oAuth2User.getAttributes().get("id")));
        }
        else{
            log.info("지원하지 않은 로그인 서비스 입니다.");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password =  passwordEncoder.encode("테스트");
        String email = oAuth2UserInfo.getEmail();
        Authority role = Authority.ROLE_USER;

        User userEntity = userRepository.findUserByEmail(username)
            .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_USER));

        if(userEntity == null) {
            userEntity = User.builder()
                .nickname(username)
                .password(password)
                .email(email)
                .role(role)
                .oauth(provider)
                .oauthId(providerId)
                .build();
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }

}
