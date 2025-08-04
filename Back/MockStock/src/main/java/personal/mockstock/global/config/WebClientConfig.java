package personal.mockstock.global.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import personal.mockstock.external.kis.config.KisConfig;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {

    /**
     * 한국투자증권 API용 WebClient (SSL 검증 무시)
     * - 개발 환경용: 한국투자증권 서버의 SSL 인증서 이슈로 인해 SSL 검증 무시
     */
    @Bean(name = "kisWebClient")
    public WebClient kisWebClient() {
        try {
            // SSL 인증서 검증 무시 (한국투자증권 API 서버 이슈 해결용)
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

            return WebClient.builder()
                    .baseUrl(KisConfig.KIS_BASE_URL)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .exchangeStrategies(ExchangeStrategies.builder()
                            .codecs(clientCodecConfigurer ->
                                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                            .build())
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException("Failed to create SSL context for KIS API", e);
        }
    }

    /**
     * KRX API용 WebClient (정상 SSL 검증)
     */
    @Bean(name = "krxWebClient")
    public WebClient krxWebClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer ->
                                clientCodecConfigurer.defaultCodecs().maxInMemorySize(30 * 1024 * 1024)) // 30MB
                        .build())
                .build();
    }

    /**
     * 기본 WebClient (KIS API용으로 사용)
     */
    @Bean
    public WebClient webClient() {
        return kisWebClient();
    }
}