package personal.mockstock.external.kis.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import personal.mockstock.external.kis.config.KisConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class KisTokenUtil {

    @Qualifier("kisWebClient")
    private final WebClient kisWebClient;
    private final KisConfig kisConfig;

    @Getter
    private String accessToken;
    private LocalDateTime tokenExpirationTime;

    @PostConstruct
    public void init() {
        generateTokenIfNeeded();
    }

    /**
     * 토큰이 필요할 때마다 확인 후 발급
     */
    public String getValidAccessToken() {
        if (isTokenExpired()) {
            generateNewToken();
        }
        return accessToken;
    }

    /**
     * 토큰 만료 여부 확인
     */
    private boolean isTokenExpired() {
        if (accessToken == null || tokenExpirationTime == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(tokenExpirationTime.minusMinutes(5)); // 5분 여유
    }

    /**
     * 토큰이 없거나 만료된 경우에만 새로 발급
     */
    private void generateTokenIfNeeded() {
        if (isTokenExpired()) {
            generateNewToken();
        } else {
            log.info("유효한 토큰이 이미 존재합니다.");
        }
    }

    /**
     * 새로운 토큰 발급
     */
    private void generateNewToken() {
        log.info("한국투자증권 API 토큰 발급 시작");

        try {
            TokenRequest request = new TokenRequest(kisConfig);

            TokenResponse response = kisWebClient
                    .post()
                    .uri(KisConfig.KIS_BASE_URL + KisConfig.TOKEN_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();

            if (response != null && response.getAccessToken() != null) {
                this.accessToken = response.getAccessToken();
                this.tokenExpirationTime = parseExpirationTime(response.getAccessTokenTokenExpired());

                log.info("토큰 발급 성공 - 만료시간: {}", tokenExpirationTime);
            } else {
                log.error("토큰 발급 실패 - 응답이 null이거나 토큰이 없음");
                throw new RuntimeException("토큰 발급 실패");
            }

        } catch (Exception e) {
            log.error("토큰 발급 중 오류 발생", e);
            throw new RuntimeException("토큰 발급 실패", e);
        }
    }

    /**
     * 토큰 만료시간 파싱
     */
    private LocalDateTime parseExpirationTime(String expirationStr) {
        try {
            if (expirationStr != null && !expirationStr.isEmpty()) {
                return LocalDateTime.parse(expirationStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            log.warn("토큰 만료시간 파싱 실패: {}", expirationStr, e);
        }

        // 파싱 실패시 현재시간 + 12시간으로 설정
        return LocalDateTime.now().plusHours(12);
    }

    /**
     * 토큰 요청 DTO
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenRequest {
        @JsonProperty("grant_type")
        private final String grantType = "client_credentials";

        @JsonProperty("appkey")
        private final String appKey;

        @JsonProperty("appsecret")
        private final String appSecret;

        public TokenRequest(KisConfig kisConfig) {
            this.appKey = kisConfig.getAppKey();
            this.appSecret = kisConfig.getAppSecret();
        }
    }

    /**
     * 토큰 응답 DTO
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("expires_in")
        private String expiresIn;

        @JsonProperty("access_token_token_expired")
        private String accessTokenTokenExpired;
    }
}