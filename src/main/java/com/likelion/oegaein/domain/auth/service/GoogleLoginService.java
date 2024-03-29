package com.likelion.oegaein.domain.auth.service;

import com.likelion.oegaein.domain.auth.dto.GoogleInfoDto;
import com.likelion.oegaein.domain.auth.dto.GoogleRequestDto;
import com.likelion.oegaein.domain.auth.dto.GoogleResponseDto;
import com.likelion.oegaein.domain.auth.exception.CustomException;
import com.likelion.oegaein.domain.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.likelion.oegaein.domain.auth.exception.CustomErrorCode.HUFS_EMAIL_ERROR;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GoogleLoginService {
    private final RestTemplate restTemplate;
    private final MemberService memberService;
    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.client-secret}")
    private String googleClientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;
    @Value("${google.token-uri}")
    private String tokenUri;
    @Value("${google.resource-uri}")
    private String resourceUri;

    public String requestUrl() {
        log.info("let take URL");
        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=" + redirectUri + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        log.info("generate URL");
        return reqUrl;
    }

    public String access(String authCode) {
        // 토큰 생성
        GoogleResponseDto jwtToken = requestAccessToken(authCode);
        log.info("jwtToken: " + jwtToken.getAccessToken());

        // 사용자 정보 반환
        GoogleInfoDto googleInfoDto = requestGoogleInfo(jwtToken);
        assert googleInfoDto != null;

        // 외대 메일 검증
        isHufsEmail(googleInfoDto.getEmail());
        /* 알림 창 표시 후, 로그인 화면으로 redirect */

        // DB에 메일 없으면 가입 진행
        String email = googleInfoDto.getEmail();
        if (memberService.findMemberByEmail(email) == null){
            memberService.join(googleInfoDto, jwtToken);
            log.info("signup email: " + email);
        }
        log.info("access email: " + email);
        return email;
    }

    private GoogleResponseDto requestAccessToken(String code) {
        GoogleRequestDto googleRequest = GoogleRequestDto.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(code)
                .redirectUri(redirectUri)
                .grantType("authorization_code").build();
        ResponseEntity<GoogleResponseDto> googleResponseEntity = restTemplate.postForEntity(tokenUri,
                googleRequest, GoogleResponseDto.class);
        return googleResponseEntity.getBody();
    }

    private GoogleInfoDto requestGoogleInfo(GoogleResponseDto token) {
        Map<String, String> map = new HashMap<>();
        map.put("id_token",token.getIdToken());
        ResponseEntity<GoogleInfoDto> googleInfoEntity = restTemplate.postForEntity(resourceUri,
                map, GoogleInfoDto.class);
        return googleInfoEntity.getBody();
    }

//    private GoogleResponseDto getTokenByAccessToken(String accessToken) {
//
//    }

    private void isHufsEmail(String email) {
        if (email.endsWith("@hufs.ac.kr")) {
            log.info(email);
        } else {
            log.info("Not HUFS Email");
            throw new CustomException(HUFS_EMAIL_ERROR);
        }
    }
}
