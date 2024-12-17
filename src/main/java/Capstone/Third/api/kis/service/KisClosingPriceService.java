package Capstone.Third.api.kis.service;

import Capstone.Third.api.kis.config.KisConfig;
import Capstone.Third.api.kis.response.ClosingPriceRes;
import Capstone.Third.api.kis.util.KisAccessTokenUtil;
import Capstone.Third.stock.entity.Stock;
import Capstone.Third.stock.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class KisClosingPriceService {
    private final String KIS_URL = "https://openapivts.koreainvestment.com:9443";

    private final WebClient webClient;

    private final KisConfig kisConfig;
    private final StockRepository stockRepository;
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
        httpHeaders.set("tr_id", "FHKST01010400");

        return httpHeaders;
    }


    public Mono<ClosingPriceRes> getClosingPriceRes(String stockId) {

        HttpHeaders headers = createPriceHttpHeaders();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/inquire-daily-price")
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                        .queryParam("FID_INPUT_ISCD", stockId)         //종몸 코드동적 할당으로 변경해야됨
                        .queryParam("FID_PERIOD_DIV_CODE", "D")        //D, W, M 일, 주, 달
                        .queryParam("FID_ORG_ADJ_PRC", "0000000001")
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(ClosingPriceRes.class)
                .doOnError(e -> log.error("error: ", e));
    }

    //제일 최근의 종가 정보를 가져오기
    public Mono<ClosingPriceRes.Output> getFirstClosingPriceRes(String stockId) {
        return getClosingPriceRes(stockId)
                .flatMap(closingPriceRes -> {
                    // 만약 리스트가 비어있지 않다면 첫 번째 데이터 반환
                    if (closingPriceRes.getOutput() != null && !closingPriceRes.getOutput().isEmpty()) {
                        return Mono.just(closingPriceRes.getOutput().get(0));
                    } else {
                        return Mono.empty(); // 비어있다면 빈 Mono 반환
                    }
                });
    }


    public Flux<ClosingPriceRes> getAllStockClosingPriceRes() {

        HttpHeaders headers = createPriceHttpHeaders();
        List<Stock> allStock = stockRepository.findAll();

        // 모든 주식 코드를 순회하면서 WebClient 요청을 생성하여 병합
        return Flux.fromIterable(allStock)
                .flatMap(stock -> webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/inquire-daily-price")
                                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                                .queryParam("FID_INPUT_ISCD", stock.getStockId()) // 각 주식 코드 동적 할당
                                .queryParam("FID_PERIOD_DIV_CODE", "D")
                                .queryParam("FID_ORG_ADJ_PRC", "0000000001")
                                .build())
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .bodyToMono(ClosingPriceRes.class)
                        .doOnError(e -> log.error("error: ", e)))
                .doOnComplete(() -> log.info("All requests completed"));
    }

//
//    //첫번째 정보들 가져오기
//    public Flux<ClosingPriceRes.Output> getAllStockFirstClosingPriceRes() {
//        return getAllStockClosingPriceRes()
//                .flatMap(closingPriceRes -> {
//                    // 만약 리스트가 비어있지 않다면 첫 번째 데이터 반환
//                    if (closingPriceRes.getOutput() != null && !closingPriceRes.getOutput().isEmpty()) {
//                        Stock stock = new Stock();
//                        stock.setStockPrice(Integer.valueOf(closingPriceRes.getOutput().get(0).getStck_clpr()));
//                        stock.setRate(closingPriceRes.getOutput().get(0).getPrdy_ctrt());
//                        stock.setChanges(closingPriceRes.getOutput().get(0).getPrdy_vrss());
//
//                        stockRepository.save(stock);
//                        return Mono.just(closingPriceRes.getOutput().get(0));
//                    } else {
//                        return Mono.empty(); // 비어있다면 빈 Mono 반환
//                    }
//                });
//
//    }
//
//    public Flux<ClosingPriceRes> saveAllStockPrice() {
//
//        HttpHeaders headers = createPriceHttpHeaders();
//        List<Stock> allStock = stockRepository.findAll();
//
//        // 모든 주식 코드를 순회하면서 WebClient 요청을 생성하여 병합
//        return Flux.fromIterable(allStock)
//                .flatMap(stock -> webClient.get()
//                        .uri(uriBuilder -> uriBuilder.path("/uapi/domestic-stock/v1/quotations/inquire-daily-price")
//                                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
//                                .queryParam("FID_INPUT_ISCD", stock.getStockId()) // 각 주식 코드 동적 할당
//                                .queryParam("FID_PERIOD_DIV_CODE", "D")
//                                .queryParam("FID_ORG_ADJ_PRC", "0000000001")
//                                .build())
//                        .headers(httpHeaders -> httpHeaders.addAll(headers))
//                        .retrieve()
//                        .bodyToMono(ClosingPriceRes.class)
//                        .doOnError(e -> log.error("error: ", e)))
//                .doOnComplete(() -> log.info("All requests completed"));
//
//
//    }
//
//
//
//
//
//    //첫번째 정보들 가져오기
//    public Flux<ClosingPriceRes.Output> saveAllStockPriceRes() {
//        return saveAllStockPrice()
//                .flatMap(closingPriceRes -> {
//                    // 만약 리스트가 비어있지 않다면 첫 번째 데이터 반환
//                    if (closingPriceRes.getOutput() != null && !closingPriceRes.getOutput().isEmpty()) {
//                        Stock stock = new Stock();
//                        stock.setStockPrice(Integer.valueOf(closingPriceRes.getOutput().get(0).getStck_clpr()));
//                        stock.setRate(closingPriceRes.getOutput().get(0).getPrdy_ctrt());
//                        stock.setChanges(closingPriceRes.getOutput().get(0).getPrdy_vrss());
//
//                        stockRepository.save(stock);
//                        return Mono.just(closingPriceRes.getOutput().get(0));
//                    } else {
//                        return Mono.empty(); // 비어있다면 빈 Mono 반환
//                    }
//                });
//    }
}
