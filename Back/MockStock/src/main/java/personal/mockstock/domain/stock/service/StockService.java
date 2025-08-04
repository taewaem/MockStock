package personal.mockstock.domain.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.mockstock.domain.stock.dto.StockResponse;
import personal.mockstock.domain.stock.entity.Stock;
import personal.mockstock.domain.stock.repository.StockRepository;
import personal.mockstock.global.dto.PageResponse;
import personal.mockstock.global.exception.ErrorCode;
import personal.mockstock.global.exception.MockStockException;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository stockRepository;

    /**
     * 전체 주식 목록 페이징 조회
     */
    public PageResponse<StockResponse> getAllStocks(int page, int size, String sortBy, String sortDir) {
        log.info("전체 주식 목록 페이징 조회: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);

        // 정렬 방향 설정
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        // 정렬 기준 설정 (종목명)
        String sortField = getSortField(sortBy);
        Sort sort = Sort.by(direction, sortField);

        // 페이지 요청 생성
        Pageable pageable = PageRequest.of(page, size, sort);

        // DB에서 페이징 조회
        Page<Stock> stockPage = stockRepository.findAll(pageable);

        // DTO 변환
        List<StockResponse> content = stockPage.getContent().stream()
                .map(this::convertToResponse)
                .toList();

        log.info("전체 주식 조회 완료: {}개 조회 (전체 {}개 중 {}페이지)",
                content.size(), stockPage.getTotalElements(), page + 1);

        return PageResponse.from(stockPage, content);
    }

    /**
     * 주식 검색
     */
    public PageResponse<StockResponse> searchStocks(String keyword, int page, int size,
                                                    String sortBy, String sortDir) {
        log.info("주식 검색 요청: keyword={}, page={}, size={}", keyword, page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStocks(page, size, sortBy, sortDir);
        }

        // 정렬 설정
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = getSortField(sortBy);
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(page, size, sort);

        // 검색 실행
        Page<Stock> stockPage = stockRepository.findByKeyword(keyword.trim(), pageable);

        // DTO 변환
        List<StockResponse> content = stockPage.getContent().stream()
                .map(this::convertToResponse)
                .toList();

        log.info("주식 검색 완료: keyword='{}', {}개 검색됨 (전체 {}개 중)",
                keyword, content.size(), stockPage.getTotalElements());

        return PageResponse.from(stockPage, content);
    }

    /**
     * 시장별 주식 조회
     */
    public PageResponse<StockResponse> getStocksByMarket(String marketCode, int page, int size,
                                                         String sortBy, String sortDir) {
        log.info("시장별 주식 조회: marketCode={}, page={}, size={}", marketCode, page, size);

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = getSortField(sortBy);
        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Stock> stockPage = stockRepository.findByMarketCode(marketCode, pageable);

        List<StockResponse> content = stockPage.getContent().stream()
                .map(this::convertToResponse)
                .toList();

        return PageResponse.from(stockPage, content);
    }

    /**
     * 주식 상세 조회
     */
    public StockResponse getStock(String stockId) {
        log.info("주식 상세 조회: stockId={}", stockId);

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new MockStockException(ErrorCode.STOCK_NOT_FOUND));

        return convertToResponse(stock);
    }

    /**
     * 랜덤 주식 추천
     */
    public List<StockResponse> getRandomStocks(int count) {
        log.info("랜덤 주식 추천 요청: count={}", count);

        List<Stock> allStocks = stockRepository.findAll();

        if (allStocks.isEmpty()) {
            log.warn("DB에 주식 데이터가 없습니다.");
            return Collections.emptyList();
        }

        Collections.shuffle(allStocks);

        return allStocks.stream()
                .limit(Math.min(count, allStocks.size()))
                .map(this::convertToResponse)
                .toList();
    }

    /**
     * 정렬 필드 검증 및 변환
     */
    private String getSortField(String sortBy) {
        return switch (sortBy) {
            case "name" -> "stockName";
            case "price" -> "currentPrice";
            case "code" -> "stockId";
            case "market" -> "marketCode";
            case "change" -> "priceChange";
            default -> "stockName"; // 기본값: 종목명
        };
    }

    /**
     * Stock 엔티티를 StockResponse DTO로 변환
     */
    private StockResponse convertToResponse(Stock stock) {
        return StockResponse.builder()
                .stockId(stock.getStockId())
                .stockName(stock.getStockName())
                .currentPrice(stock.getCurrentPrice())
                .priceChange(stock.getPriceChange())
                .changeRate(stock.getChangeRate())
                .marketCode(stock.getMarketCode())
                .build();
    }
}