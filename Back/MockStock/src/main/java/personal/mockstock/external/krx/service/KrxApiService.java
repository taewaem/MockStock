package personal.mockstock.external.krx.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import personal.mockstock.external.krx.dto.KrxPriceResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class KrxApiService {

    @Value("${krx.serviceKey}")
    private String serviceKey;

    private final String KRX_BASE_URL = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService";

    @Qualifier("krxWebClient")
    private final WebClient krxWebClient;


    private final WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(clientCodecConfigurer ->
                            clientCodecConfigurer.defaultCodecs().maxInMemorySize(30 * 1024 * 1024))
                    .build())
            .baseUrl(KRX_BASE_URL)
            .build();

    /**
     * 전체 주식 종가 정보 조회
     */
    public KrxPriceResponse getStockPrice() {
        try {
            log.info("KRX API 호출 시작 - 전체 주식 종가 조회");

            KrxPriceResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/getStockPriceInfo")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("resultType", "json")
                            .queryParam("numOfRows", "2500")
                            .queryParam("pageNo", "1")
                            .build())
                    .header("Accept", "application/json")
                    .retrieve()
                    .bodyToMono(KrxPriceResponse.class)
                    .block();

            if (response == null) {
                log.warn("KRX API 응답이 null입니다.");
                return createEmptyResponse();
            }

            log.info("KRX API 호출 완료 - 조회된 종목 수: {}",
                    response.getResponse() != null &&
                            response.getResponse().getBody() != null ?
                            response.getResponse().getBody().getTotalCount() : 0);


            return response;

        } catch (Exception e) {
            log.error("KRX API 호출 중 오류 발생", e);
            return createEmptyResponse();
        }
    }

    /**
     * API 응답 유효성 검사
     */
    public boolean isValidResponse(KrxPriceResponse response) {
        return response != null &&
                response.getResponse() != null &&
                response.getResponse().getBody() != null &&
                response.getResponse().getBody().getItems() != null &&
                response.getResponse().getBody().getItems().getItemList() != null &&
                !response.getResponse().getBody().getItems().getItemList().isEmpty();
    }

    /**
     * 빈 응답 생성 (API 실패시 사용
     */
    private KrxPriceResponse createEmptyResponse() {
        return new KrxPriceResponse();
    }
}