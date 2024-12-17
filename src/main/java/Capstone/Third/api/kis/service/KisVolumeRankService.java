    //    package Capstone.Third.api.kis.service;
//
//    import Capstone.Third.api.kis.config.KisConfig;
//    import Capstone.Third.api.kis.response.KisVolumeRankRes;
//    import com.fasterxml.jackson.databind.JsonNode;
//    import com.fasterxml.jackson.databind.ObjectMapper;
//    import io.netty.handler.ssl.SslContextBuilder;
//    import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
//    import lombok.RequiredArgsConstructor;
//    import lombok.extern.slf4j.Slf4j;
//    import org.springframework.beans.factory.annotation.Autowired;
//    import org.springframework.http.HttpHeaders;
//    import org.springframework.http.MediaType;
//    import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//    import org.springframework.stereotype.Component;
//    import org.springframework.stereotype.Service;
//    import org.springframework.web.reactive.function.client.WebClient;
//    import reactor.core.publisher.Mono;
//    import reactor.netty.http.client.HttpClient;
//
//    import java.util.ArrayList;
//    import java.util.List;
//
//
//    @Slf4j
//    @Service
//    @RequiredArgsConstructor
//    public class KisVolumeRankService {
//
//        private final String KIS_URL = "https://openapivts.koreainvestment.com:9443";
//        private final WebClient webClient;
//    //    = WebClient
//    //            .builder()
//    //            .baseUrl(KIS_URL)
//    //            .build();
//
//        private final KisConfig kisConfig;
//
//        private final ObjectMapper objectMapper;
//
//
//    //    private String accessToken = kisAccessTokenService.getAccessToken;
//        private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b2tlbiIsImF1ZCI6IjUzOGRjZjlkLTQ1MjgtNDlhZi05YTRlLWIyMzc1ZWY1OTI5NCIsInByZHRfY2QiOiIiLCJpc3MiOiJ1bm9ndyIsImV4cCI6MTcyNzMzNzMzOSwiaWF0IjoxNzI3MjUwOTM5LCJqdGkiOiJQU2gzVE0zNGh4dE5yRVpLVlVjbHlHeDZOeENwdUF0cUZ5V0MifQ._07Od0t2G_0Z28wHI5e6RPpSg-03XdDEcFajzYUH89aV0zg0VYXRAbWcWYt5QMjtWpmQfX5oo9x3KDE0PslZWA";
//
//        //SSL 무시 개발 환경에서만 사용!
//        @Autowired
//        public KisVolumeRankService(KisConfig kisConfig, ObjectMapper objectMapper) {
//            this.kisConfig = kisConfig;
//            this.objectMapper = objectMapper;
//
//            this.webClient = WebClient.builder()
//                    .baseUrl(KIS_URL)
//                    .clientConnector(new ReactorClientHttpConnector(
//                            HttpClient.create()
//                                    .secure(sslContextSpec -> sslContextSpec.sslContext(
//                                            SslContextBuilder.forClient()
//                                                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                                    ))
//                    ))
//                    .build();
//        }
//
//        private HttpHeaders createVolumnRankHttpHeaders() {
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(accessToken);
//            headers.set("appkey", kisConfig.getAppKey());
//            headers.set("appsecret", kisConfig.getAppSecret());
//            headers.set("tr_id", "FHPST01710000");
//            headers.set("custtype", "P");
//
//            return headers;
//        }
//
//
//        // 응답 JSON을 파싱하는 메소드
//        private Mono<List<KisVolumeRankRes.Output1>> parseFVolumeRank(String response) {
//            try {
//                List<KisVolumeRankRes.Output1> responseDataList = new ArrayList<>();
//                JsonNode rootNode = objectMapper.readTree(response);
//                JsonNode outputNode = rootNode.get("output");
//
//                if (outputNode != null) {
//                    for (JsonNode node : outputNode) {
//                        KisVolumeRankRes.Output1 responseData = KisVolumeRankRes.Output1.builder()
//                                .hts_kor_isnm(node.get("hts_kor_isnm").asText())
//                                .mksc_shrn_iscd(node.get("mksc_shrn_iscd").asText())
//                                .data_rank(node.get("data_rank").asText())
//                                .stck_prpr(node.get("stck_prpr").asText())
//                                .prdy_vrss_sign(node.get("prdy_vrss_sign").asText())
//                                .prdy_vrss(node.get("prdy_vrss").asText())
//                                .prdy_ctrt(node.get("prdy_ctrt").asText())
//                                .acml_vol(node.get("acml_vol").asText())
//                                .prdy_vol(node.get("prdy_vol").asText())
//                                .lstn_stcn(node.get("lstn_stcn").asText())
//                                .avrg_vol(node.get("avrg_vol").asText())
//                                .n_befr_clpr_vrss_prpr_rate(node.get("n_befr_clpr_vrss_prpr_rate").asText())
//                                .vol_inrt(node.get("vol_inrt").asText())
//                                .vol_tnrt(node.get("vol_tnrt").asText())
//                                .nday_vol_tnrt(node.get("nday_vol_tnrt").asText())
//                                .avrg_tr_pbmn(node.get("avrg_tr_pbmn").asText())
//                                .tr_pbmn_tnrt(node.get("tr_pbmn_tnrt").asText())
//                                .nday_tr_pbmn_tnrt(node.get("nday_tr_pbmn_tnrt").asText())
//                                .acml_tr_pbmn(node.get("acml_tr_pbmn").asText())
//                                .build();
//
//                        responseDataList.add(responseData);
//                    }
//                }
//
//                return Mono.just(responseDataList);
//            } catch (Exception e) {
//                return Mono.error(e);
//            }
//        }
//
//        public Mono<List<KisVolumeRankRes.Output1>> getVolumeRank() {
//            HttpHeaders headers = createVolumnRankHttpHeaders();
//
//            return webClient.get()
//                    .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/volume-rank")
//                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")
//                            .queryParam("FID_COND_SCR_DIV_CODE", "20171")
//                            .queryParam("FID_INPUT_ISCD", "0002")
//                            .queryParam("FID_DIV_CLS_CODE", "0")
//                            .queryParam("FID_BLNG_CLS_CODE", "0")
//                            .queryParam("FID_TRGT_CLS_CODE", "111111111")
//                            .queryParam("FID_TRGT_EXLS_CLS_CODE", "000000")
//                            .queryParam("FID_INPUT_PRICE_1", "0")
//                            .queryParam("FID_INPUT_PRICE_2", "0")
//                            .queryParam("FID_VOL_CNT", "0")
//                            .queryParam("FID_INPUT_DATE_1", "0")
//                            .build())
//                    .headers(httpHeaders -> httpHeaders.addAll(headers))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .flatMap(this::parseFVolumeRank)
//                    .doOnError(e -> log.error("Error occurred during getVolumeRank: ", e));
//        }
//
//    }

package Capstone.Third.api.kis.service;

import Capstone.Third.api.kis.config.KisConfig;
import Capstone.Third.api.kis.response.KisVolumeRankRes;
import Capstone.Third.api.kis.util.KisAccessTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KisVolumeRankService {

    private final WebClient webClient;
    private final KisConfig kisConfig;
    private final KisAccessTokenUtil kisAccessTokenUtil;

    private String accessToken;

    @PostConstruct
    public void init() {
        this.accessToken = kisAccessTokenUtil.getAccessToken();
    }

    /**
     * httphaders 메서드
     * @return
     */
    private HttpHeaders createVolumnRankHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + accessToken);
        headers.set("appkey", kisConfig.getAppKey());
        headers.set("appsecret", kisConfig.getAppSecret());
        headers.set("tr_id", "FHPST01710000");
        headers.set("custtype", "P");
        return headers;
    }

    public Mono<KisVolumeRankRes> getVolumeRank() {
        HttpHeaders headers = createVolumnRankHttpHeaders();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/volume-rank")
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                        .queryParam("FID_COND_SCR_DIV_CODE", "20171")
                        .queryParam("FID_INPUT_ISCD", "0000")
                        .queryParam("FID_DIV_CLS_CODE", "0")
                        .queryParam("FID_BLNG_CLS_CODE", "0")
                        .queryParam("FID_TRGT_CLS_CODE", "111111111")
                        .queryParam("FID_TRGT_EXLS_CLS_CODE", "000000")
                        .queryParam("FID_INPUT_PRICE_1", "0")
                        .queryParam("FID_INPUT_PRICE_2", "0")
                        .queryParam("FID_VOL_CNT", "0")
                        .queryParam("FID_INPUT_DATE_1", "0")
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(KisVolumeRankRes.class);
    }
}
