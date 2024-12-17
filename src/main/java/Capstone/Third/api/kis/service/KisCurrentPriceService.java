package Capstone.Third.api.kis.service;


import Capstone.Third.api.kis.config.KisConfig;
import Capstone.Third.api.kis.response.CurrentPriceRes;
import Capstone.Third.api.kis.util.KisAccessTokenUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KisCurrentPriceService {
    private final String KIS_URL = "https://openapivts.koreainvestment.com:9443";

    private final WebClient webClient;

    private final KisConfig kisConfig;

    private final KisAccessTokenUtil kisAccessTokenUtil;

    private String accessToken;

    @PostConstruct
    public void init() {
        this.accessToken = kisAccessTokenUtil.getAccessToken();
    }


    private HttpHeaders createPriceHttpHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("authorization", "Bearer " + accessToken);
        httpHeaders.set("appkey", kisConfig.getAppKey());
        httpHeaders.set("appsecret", kisConfig.getAppSecret());
        httpHeaders.set("tr_id", "FHKST01010100");

        return httpHeaders;
    }


    public Mono<CurrentPriceRes> getPrice(String stockId) {

        HttpHeaders headers = createPriceHttpHeaders();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/inquire-price")
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                        .queryParam("FID_INPUT_ISCD", stockId)
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(CurrentPriceRes.class)
                .doOnError(e -> log.error("error: ", e));
    }


}
