package personal.mockstock.domain.stock.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.mockstock.domain.stock.entity.Stock;
import personal.mockstock.domain.stock.repository.StockRepository;
import personal.mockstock.external.krx.dto.KrxPriceResponse;
import personal.mockstock.external.krx.service.KrxApiService;

import java.util.List;

/**
 * 주식 데이터 동기화 서비스
 * 책임: 외부 API에서 데이터를 가져와 DB에 동기화 (초기화 + 스케줄링)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StockSyncService {

    private final KrxApiService krxApiService;
    private final StockRepository stockRepository;

    /**
     * 서버 시작시 주식 데이터 초기화
     */
    @PostConstruct
    public void initializeStockData() {

        try {
            log.info("=== 서버 시작: 주식 데이터 초기화 시작 ===");
            syncStockData();
        } catch (Exception e) {
            log.error("주식 데이터 초기화 중 오류 발생", e);
        }
    }

    /**
     * 매일 오후 4시 30분에 주식 데이터 업데이트
     */
    @Scheduled(cron = "0 30 16 * * MON-FRI", zone = "Asia/Seoul")
    public void scheduledSync() {
        log.info("=== 스케줄러: 주식 데이터 동기화 시작 ===");

        try {
            syncStockData();
            log.info("=== 스케줄러: 주식 데이터 동기화 완료 ===");
        } catch (Exception e) {
            log.error("=== 스케줄러: 주식 데이터 동기화 실패 ===", e);
        }
    }

    @Transactional
    public void syncStockData() {
        log.info("KRX API에서 주식 데이터 동기화 시작");

        try {
            KrxPriceResponse krxResponse = krxApiService.getStockPrice();

            if (!krxApiService.isValidResponse(krxResponse)) {
                log.warn("KRX API 응답이 유효하지 않습니다. 동기화 중단");
                return;
            }

            List<KrxPriceResponse.KrxItem> stockItems = krxResponse.getResponse()
                    .getBody().getItems().getItemList();

            log.info("KRX API에서 {} 개 종목 데이터 조회 완료", stockItems.size());

            int updateCount = 0;
            int insertCount = 0;

            for (KrxPriceResponse.KrxItem krxItem : stockItems) {
                try {
                    Stock existingStock = stockRepository.findById(krxItem.getSrtnCd()).orElse(null);

                    if (existingStock != null) {
                        updateExistingStock(existingStock, krxItem);
                        updateCount++;
                    } else {
                        Stock newStock = createStockFromKrx(krxItem);
                        stockRepository.save(newStock);
                        insertCount++;
                    }

                } catch (Exception e) {
                    log.error("종목 동기화 중 오류 발생: stockId={}, error={}",
                            krxItem.getSrtnCd(), e.getMessage());
                }
            }

            log.info("주식 데이터 동기화 완료 - 신규: {}개, 업데이트: {}개", insertCount, updateCount);

        } catch (Exception e) {
            log.error("주식 데이터 동기화 중 오류 발생", e);
            throw new RuntimeException("주식 데이터 동기화 실패", e);
        }
    }

    private void updateExistingStock(Stock existingStock, KrxPriceResponse.KrxItem krxItem) {
        if (!existingStock.getStockName().equals(krxItem.getItmsNm())) {
            existingStock.updateStockName(krxItem.getItmsNm());
        }

        Integer price = parsePrice(krxItem.getClpr());
        existingStock.updatePrice(price, krxItem.getVs(), krxItem.getFltRt());

        stockRepository.save(existingStock);
    }

    private Stock createStockFromKrx(KrxPriceResponse.KrxItem krxItem) {
        return Stock.builder()
                .stockId(krxItem.getSrtnCd())
                .stockName(krxItem.getItmsNm())
                .marketCode(krxItem.getMrktCtg())
                .currentPrice(parsePrice(krxItem.getClpr()))
                .priceChange(krxItem.getVs())
                .changeRate(krxItem.getFltRt())
                .build();
    }

    private Integer parsePrice(String priceStr) {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.valueOf(priceStr.replace(",", ""));
        } catch (NumberFormatException e) {
            log.warn("가격 변환 실패: {}", priceStr);
            return 0;
        }
    }
}