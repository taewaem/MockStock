package Capstone.Third.api.kis.util;


import Capstone.Third.api.kis.config.KisConfig;
import Capstone.Third.api.kis.exception.KisTokenRequestException;
import Capstone.Third.api.kis.request.KisTokenCreateReq;
import Capstone.Third.api.kis.response.KisTokenCreateRes;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class KisAccessTokenUtil {

    private final String KIS_URL = "https://openapivts.koreainvestment.com:9443";
    private final WebClient webClient;


    private final KisConfig kisConfig;

    @Getter
    private String accessToken;

    // 토큰 만료 시간 저장 변수
    private LocalDateTime tokenExpirationTime;

    // 토큰이 없거나 만료된 경우에만 새로운 토큰 발급
    @PostConstruct
    public void init() {
        if (accessToken == null || isTokenExpired()) {
            generateNewToken();
        } else {
            log.info("The token already exists.");
        }
    }

    // 토큰 발급 메서드
    private void generateNewToken() {
        KisTokenCreateReq kisTokenCreateReq = new KisTokenCreateReq(kisConfig);

        try {
            KisTokenCreateRes kisTokenCreateRes = webClient
                    .post()
                    .uri(KIS_URL + "/oauth2/tokenP")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(kisTokenCreateReq)
                    .retrieve()
                    .bodyToMono(KisTokenCreateRes.class)
                    .block();

            accessToken = kisTokenCreateRes.getAccessToken();

            // 토큰 만료 시간 파싱
            String tokenExpiration = kisTokenCreateRes.getAccessTokenTokenExpired();
            tokenExpirationTime = LocalDateTime.parse(tokenExpiration, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            log.info("A new token has been issued: {}", accessToken);
            log.info("token expiration time: {}", tokenExpirationTime);
        } catch (Exception e) {
            throw new KisTokenRequestException("Token issuance failure", e);
        }
    }

    // 토큰 만료 확인 메서드
    private boolean isTokenExpired() {
        return tokenExpirationTime == null || LocalDateTime.now().isAfter(tokenExpirationTime);
    }
}

//            WebClient.builder()
//                    .baseUrl(KIS_URL)
//                    .build();


//    @PostConstruct
//    public void init(){
//        generateAccessTokenIfNotExist();
//    }
//
//    // 토큰이 없을 때만 생성하는 메소드
//    public void generateAccessTokenIfNotExist() {
//        if (accessToken == null || accessToken.isEmpty()) {
//            KisTokenCreateReq kisTokenCreateReq = new KisTokenCreateReq(kisConfig);
//
//            try {
//                KisTokenCreateRes kisTokenCreateRes = webClient
//                        .post()
//                        .uri("/oauth2/tokenP")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .retrieve()
//                        .bodyToMono(KisTokenCreateRes.class)
//                        .block();
//
//                accessToken = kisTokenCreateRes.getAccessToken();
//                log.info("kis 접근 토큰 발급 완료: {}", LocalDateTime.now());
//            } catch (Exception e) {
//                throw new KisTokenRequestException();
//            }
//        } else {
//            log.info("기존 토큰이 이미 존재합니다.");
//        }
//    }

//    @PostConstruct  //bean이 생성된 이후에 한 번만 실행된다.
//    @Scheduled(cron = "0 0 0 * * *",zone = "Asia/Seoul")    //매일 자정 토근 발급
//    public void init(){
//        KisTokenCreateReq kisTokenCreateReq = new KisTokenCreateReq(kisConfig);
//
//        try {
//            //한국투자증권에서 accesstoken 얻어오는 연결
//            KisTokenCreateRes kisTokenCreateRes = webClient
//                    //post 요청
//                    .post()
//                    .uri("/oauth2/tokenP")
//                    //JSON형식으로 요청
//                    .contentType(MediaType.APPLICATION_JSON)
//                    //API응답수신
//                    .retrieve()
//                    //응답을 KisTokenCreateRes로 변환
//                    .bodyToMono(KisTokenCreateRes.class)
//                    .block();
//
//            accessToken = kisTokenCreateRes.getAccessToken();
//            log.info("kis 접근 토큰 발급 완료: {}", LocalDateTime.now());
//        } catch (Exception e) {
//            //요청값이 제대로 가지 않아 한국투자증권에서 거부하는 경우
//            throw new KisTokenRequestException();
//        }
//    }


