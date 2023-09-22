package com.example.webtoon.user.security;

import com.example.webtoon.user.service.PrincipalService;
import com.example.webtoon.user.type.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 6; // token 기간 6시간
    private final PrincipalService principalService;
    private static final String KEY_ROLE = "role";

    @Value("${spring.jwt.secret}")
    private String secretKey;

    public String generateToken(String email, Authority authority) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLE, authority);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.HS256, this.secretKey)
            .compact();
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = parseClaims(jwt);
        UserDetails userDetails = this.principalService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(
            userDetails, jwt, userDetails.getAuthorities());
    }

    public String getUserId(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
