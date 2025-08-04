package personal.mockstock.external.kis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import personal.mockstock.external.kis.config.KisConfig;
import personal.mockstock.external.kis.dto.KisVolumeRankResponse;
import personal.mockstock.external.kis.util.KisTokenUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class KisApiService {

    @Qualifier("kisWebClient")
    private final WebClient kisWebClient;
    private final KisConfig kisConfig;
    private final KisTokenUtil kisTokenUtil;

    /**
     * 거래량 랭킹 조회 (상위 30개)
     */
    public KisVolumeRankResponse getVolumeRanking() {
        log.info("한국투자증권 API 거래량 랭킹 조회 시작");

        try {
            // 유효한 토큰 확보
            String accessToken = kisTokenUtil.getValidAccessToken();

            // HTTP 헤더 설정
            HttpHeaders headers = createVolumeRankHeaders(accessToken);

            // API 호출
            KisVolumeRankResponse response = kisWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path(KisConfig.VOLUME_RANK_PATH)
                            .queryParam("FID_COND_MRKT_DIV_CODE", "J")      // 주식시장
                            .queryParam("FID_COND_SCR_DIV_CODE", "20171")   // 거래량 랭킹
                            .queryParam("FID_INPUT_ISCD", "0000")           // 전체 종목
                            .queryParam("FID_DIV_CLS_CODE", "0")            // 전체
                            .queryParam("FID_BLNG_CLS_CODE", "0")           // 전체
                            .queryParam("FID_TRGT_CLS_CODE", "111111111")   // 대상 구분
                            .queryParam("FID_TRGT_EXLS_CLS_CODE", "000000") // 제외 구분
                            .queryParam("FID_INPUT_PRICE_1", "0")           // 시작가격
                            .queryParam("FID_INPUT_PRICE_2", "0")           // 끝가격
                            .queryParam("FID_VOL_CNT", "0")                 // 거래량
                            .queryParam("FID_INPUT_DATE_1", "0")            // 기준일자
                            .build())
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .bodyToMono(KisVolumeRankResponse.class)
                    .block();

            if (response != null && response.isSuccess()) {
                log.info("거래량 랭킹 조회 성공: {}개 종목",
                        response.hasValidData() ? response.getOutput().size() : 0);
                return response;
            } else {
                log.warn("거래량 랭킹 조회 실패: rtCd={}, msg={}",
                        response != null ? response.getRtCd() : "null",
                        response != null ? response.getMsg1() : "null");
                return createEmptyResponse();
            }

        } catch (Exception e) {
            log.error("거래량 랭킹 API 호출 중 오류 발생", e);
            return createEmptyResponse();
        }
    }

    /**
     * 거래량 랭킹 API용 HTTP 헤더 생성
     */
    private HttpHeaders createVolumeRankHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + accessToken);
        headers.set("appkey", kisConfig.getAppKey());
        headers.set("appsecret", kisConfig.getAppSecret());
        headers.set("tr_id", "FHPST01710000");    // 거래량 랭킹 조회 TR_ID
        headers.set("custtype", "P");             // 개인
        return headers;
    }

    /**
     * API 응답 유효성 검사
     */
    public boolean isValidResponse(KisVolumeRankResponse response) {
        return response != null &&
                response.isSuccess() &&
                response.hasValidData();
    }

    /**
     * 빈 응답 생성 (API 실패시 사용)
     */
    private KisVolumeRankResponse createEmptyResponse() {
        return new KisVolumeRankResponse();
    }
}