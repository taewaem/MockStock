//package Capstone.Third.api.kis.service;
//
//
//import Capstone.Third.api.kis.config.KisConfig;
//import Capstone.Third.api.kis.response.StockInfoRes;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.netty.http.client.HttpClient;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class StockInfoService {
//
//    private static final String KIS_URL = "https://openapivts.koreainvestment.com:9443";
//
//    private final WebClient webClient;
//
//    private final KisConfig kisConfig;
//
//    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6Ijk0ZWRjMGYxLTI5OTQtNDNjMS04MTcyLWViODA0MTU2ZWZmNiIsInByZHRfY2QiOiIiLCJpc3MiOiJ1bm9ndyIsImV4cCI6MTcyNzUyMDI5NywiaWF0IjoxNzI3NDMzODk3LCJqdGkiOiJQU2gzVE0zNGh4dE5yRVpLVlVjbHlHeDZOeENwdUF0cUZ5V0MifQ.gdVMn3jep2ntLRE91VLH79cHYiHhjzG9HfzO3Z_JsU3lOcGny7jo95QN8pA6DlmkE1A7cMC4nQOWXL1ZGlbeQg";
//
//
//    @Autowired
//    public StockInfoService(KisConfig kisConfig, WebClient webClient) {
//        this.kisConfig =kisConfig;
//
//        // SSL 무시 설정
//        HttpClient httpClient = HttpClient.create()
//                .secure(sslContextSpec -> sslContextSpec.sslContext(
//                        SslContextBuilder.forClient()
//                                .trustManager(InsecureTrustManagerFactory.INSTANCE) // 신뢰할 수 없는 인증서 허용
//                ));
//
//        this.webClient = WebClient.builder()
//                .baseUrl(KIS_URL)
//                .clientConnector(new ReactorClientHttpConnector(httpClient)) // HTTP 클라이언트 설정
//                .build();
//
////        this.webClient = WebClient.builder()
////                .baseUrl(KIS_URL)
////                .build();
//    }
//
//
//    private HttpHeaders createStcokInfoHeader() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("content-Type", "application/json");
//        headers.set("authorization", "Bearer " + accessToken);
//        headers.set("appkey", kisConfig.getAppKey());
//        headers.set("appsecret", kisConfig.getAppSecret());
//        headers.set("tr_id", "CRPF1002R");
//
//        return headers;
//    }
//
//    public Mono<StockInfoRes> getStockInfo(String pdno, String accessToken) {
//
//        HttpHeaders headers = createStcokInfoHeader();
//
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/search-stock-info")
//                        .queryParam("PRDT_TYPE_CD", "300")  // 고정값
//                        .queryParam("PDNO", pdno)           // 동적으로 전달되는 pdno 값
//                        .build())
//                .retrieve()
//                .bodyToMono(StockInfoRes.class);
//    }
//
//    public Mono<StockInfoRes> getStockInfo1() {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/search-stock-info")
//                        .queryParam("PRDT_TYPE_CD", "300")  // 고정값
//                        .queryParam("PDNO", "000660")           // 동적으로 전달되는 pdno 값
//                        .build())
//                .header("content-Type", "application/json")
//                .header("authorization", "Bearer " + accessToken)
//                .header("appkey", kisConfig.getAppKey())
//                .header("appsecret", kisConfig.getAppSecret())
//                .header("tr_id", "CTPF1002R")
//                .retrieve()
//                .bodyToMono(StockInfoRes.class);
//    }
//
//}

package Capstone.Third.api.kis.service;

import Capstone.Third.api.kis.config.KisConfig;
import Capstone.Third.api.kis.response.StockInfoRes;
import Capstone.Third.api.kis.util.KisAccessTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockInfoService {

    private final WebClient webClient;
    private final KisConfig kisConfig;
    private final KisAccessTokenUtil kisAccessTokenUtil;
    private String accessToken;

    @PostConstruct
    public void init() {
        this.accessToken = kisAccessTokenUtil.getAccessToken();
    }


    private HttpHeaders createStockInfoHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + accessToken);
        headers.set("appkey", kisConfig.getAppKey());
        headers.set("appsecret", kisConfig.getAppSecret());
        headers.set("tr_id", "CTPF1002R");
        headers.set("custtype", "P");
        return headers;
    }

    //동적 처리
    public Mono<StockInfoRes> getStockInfo(String stockId) {
        HttpHeaders headers = createStockInfoHeaders();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/search-stock-info")
                        .queryParam("PRDT_TYPE_CD", "300")
                        .queryParam("PDNO", stockId)
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(StockInfoRes.class);
    }

}
