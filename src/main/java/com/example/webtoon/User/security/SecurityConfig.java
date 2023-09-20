package com.example.webtoon.User.security;

import com.example.webtoon.User.service.PrincipalOauthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final PrincipalOauthUserService principalOauthUserService;

    @Bean
    public SecurityFilterChain filerChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/**/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/loginForm") //미인증자일경우 해당 uri를 호출
            .loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 낚아 채서(post로 오는것) 대신 로그인 진행 -> 컨트롤러를 안만들어도 된다.
            .defaultSuccessUrl("/")

            .and()
            .oauth2Login()
            .loginPage("/loginForm")
            .defaultSuccessUrl("/")
            .userInfoEndpoint()
            .userService(principalOauthUserService);//구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함 . Tip.코드x, (엑세스 토큰+사용자 프로필 정보를 받아옴)
        return http.build();
    }
}
