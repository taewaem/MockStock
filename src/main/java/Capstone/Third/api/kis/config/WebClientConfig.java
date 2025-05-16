package Capstone.Third.api.kis.config;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    private final String KIS_URL = "https://openapi.koreainvestment.com:9443";
    private final String KRX_URL = "https://apis.data.go.kr/1160100/service/GetKrxListedInfoService";

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(
                        SslContextBuilder.forClient()
                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                ));

        return WebClient.builder()
                .baseUrl(KIS_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}  