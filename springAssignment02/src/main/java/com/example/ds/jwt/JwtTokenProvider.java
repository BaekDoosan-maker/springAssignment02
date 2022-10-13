package com.example.ds.jwt;

import com.example.ds.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${secret.access}")
    private String SECRET_KEY;
    @Value("${secret.refresh}")
    private String REFRESH_KEY;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TYPE = "BEARER ";

    private final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;   // 30분
    private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L;   // 1주

    private final UserDetailsServiceImpl userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        System.out.println("SECRET_KEY: "+SECRET_KEY);
        System.out.println("REFRESH_KEY = " + REFRESH_KEY);
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        REFRESH_KEY = Base64.getEncoder().encodeToString(REFRESH_KEY.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(long userId, String username) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(username);
        byte[] secreteKeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // set Expire Time
                .signWith(Keys.hmacShaKeyFor(secreteKeyBytes), SignatureAlgorithm.HS256)  // 사용할 암호화 알고리즘과
                .compact();
        return BEARER_TYPE+jwt;
    }

    public String createRefreshToken(Long id, String username) {

        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(username);
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(REFRESH_KEY);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(refreshKeyBytes), SignatureAlgorithm.HS256)
                .compact();
        return BEARER_TYPE+jwt;
    }

    // Request의 Header에서 token 값을 가져옵니다.
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(@NotNull HttpServletRequest request) {
        String bearerToken = request.getHeader("REFRESH_TOKEN");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 권한정보받기
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();

    }

    public Claims getClaimsToken(String token) {
        return Jwts.parserBuilder().setSigningKey(REFRESH_KEY).build().parseClaimsJws(token).getBody();
    }

    public boolean isValidAccessToken(String token) {
        System.out.println("isValidToken is : " +token);
        try {
            Claims accessClaims = getClaimsFormToken(token);
            System.out.println("Access expireTime: " + accessClaims.getExpiration());
            System.out.println("Access username: " + accessClaims.getSubject());
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("Token ExpiredUsername : " + exception.getClaims().getSubject());
            return false;
        } catch (JwtException exception) {
            System.out.println("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Token is null");
            return false;
        } catch(Exception e){
            System.out.println("Exception 발생");
            return false;
        }
    }
    public boolean isValidRefreshToken(String token) {
        try {
            Claims refreshClaims = getClaimsToken(token);
            System.out.println("Refresh expireTime: " + refreshClaims.getExpiration());
            System.out.println("Refresh username: " + refreshClaims.getSubject());
            return true;
        } catch (ExpiredJwtException exception) {
            System.out.println("Token Expired Username : " + exception.getClaims().getSubject());
            return false;
        } catch (JwtException exception) {
            System.out.println("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            System.out.println("Token is null");
            return false;
        }catch(Exception e){
            System.out.println("Exception 발생");
            return false;
        }

    }
}
