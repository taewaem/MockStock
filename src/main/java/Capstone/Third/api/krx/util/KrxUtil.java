package Capstone.Third.api.krx.util;

import Capstone.Third.api.krx.config.KrxConfig;
import Capstone.Third.api.krx.response.KrxPriceResponse;
import Capstone.Third.api.krx.response.KrxResponse;
import Capstone.Third.stock.entity.Stock;
import Capstone.Third.stock.repository.StockRepository;
import Capstone.Third.stock.service.StockService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

//        private String KRX_URL = "https://apis.data.go.kr/1160100/service/GetKrxListedInfoService";

//        private final WebClient krxWebClient = WebClient.builder()
//                //메모리 확장
//                .exchangeStrategies(ExchangeStrategies.builder()
//                        .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(30 * 1025 * 1024))
//                        .build())
//                .baseUrl(KRX_URL)
//                .build();

@Slf4j
@Service
@RequiredArgsConstructor
public class KrxUtil {


    private String KRX_PRICE_URL = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService";
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final KrxConfig krxConfig;


    private final WebClient krxPriceWebClient = WebClient.builder()
            //메모리 확장
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(30 * 1025 * 1024))
                    .build())
            .baseUrl(KRX_PRICE_URL)
            .build();



    public KrxPriceResponse getStockPrice() {
        return krxPriceWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/getStockPriceInfo")
                        .queryParam("serviceKey", krxConfig.getServiceKey())
                        .queryParam("resultType", "json")
                        .queryParam("numOfRows", "2697")
                        .queryParam("pageNo", "1")
                        .build())
                .header("Accept", "application/json")  // JSON 응답을 명시적으로 요청
                .retrieve()
                .bodyToMono(KrxPriceResponse.class)
                .block();
    }

    @PostConstruct
    public void init() {
        initKrx();
    }


    @Scheduled(cron = "0 16 00 * * MON-FRI", zone = "Asia/Seoul")
    public void initKrx() {
        try {
            KrxPriceResponse krxPriceResponse = getStockPrice();
            // 응답 로깅
            log.info("Received KRX response: {}", krxPriceResponse);

            // Stock 엔티티에 데이터 저장
            if (krxPriceResponse != null && krxPriceResponse.getResponse() != null) {
                List<Stock> stocks = new ArrayList<>();
                // KrxItem을 사용하여 아이템 목록을 반복합니다.
                for (KrxPriceResponse.KrxItem item : krxPriceResponse.getResponse().getBody().getItems().getItemList()) {
                    Stock stock = Stock.builder()
                            .stockId(item.getSrtnCd())
                            .stockName(item.getItmsNm())
                            .stockPrice(Integer.valueOf(item.getClpr()))
                            .changes(item.getVs())
                            .rate(item.getFltRt())
                            .marketCode(item.getMrktCtg())
                            .build();
                    stocks.add(stock);

                }
                stockRepository.saveAll(stocks);

                log.info("Saved {} stocks to the database.", stocks.size());
            }
            stockService.updateStockPrice();
        } catch (Exception e) {
            log.error("Error occurred while fetching and saving KRX data: {}", e.getMessage(), e);
        }
    }

}
