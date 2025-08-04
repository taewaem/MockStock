package personal.mockstock.external.kis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kis")
@Data
public class KisConfig {

    private String appKey;
    private String appSecret;

    // API 기본 URL
    public static final String KIS_BASE_URL = "https://openapivts.koreainvestment.com:9443";

    // 거래량 랭킹 API 경로
    public static final String VOLUME_RANK_PATH = "/uapi/domestic-stock/v1/quotations/volume-rank";

    // 토큰 발급 API 경로
    public static final String TOKEN_PATH = "/oauth2/tokenP";
}