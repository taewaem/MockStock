package personal.mockstock.domain.stock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import personal.mockstock.domain.stock.dto.StockResponse;
import personal.mockstock.domain.stock.dto.StockTradingRequest;
import personal.mockstock.domain.stock.dto.VolumeRankResponse;
import personal.mockstock.domain.stock.service.StockService;
import personal.mockstock.domain.stock.service.VolumeRankService;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.userStock.service.UserStockService;
import personal.mockstock.global.dto.ApiResponse;
import personal.mockstock.global.dto.PageResponse;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Slf4j
public class StockApiController {

    private final StockService stockService;
    private final UserStockService userStockService;
    private final VolumeRankService volumeRankService;

    /**
     * 전체 주식 목록 조회
     */
    @GetMapping
    public ApiResponse<PageResponse<StockResponse>> getAllStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("전체 주식 목록 API 호출: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);

        PageResponse<StockResponse> stocks = stockService.getAllStocks(page, size, sortBy, sortDir);

        return ApiResponse.success("주식 목록 조회 완료", stocks);
    }

    /**
     * 주식 검색
     */
    @GetMapping("/search")
    public ApiResponse<PageResponse<StockResponse>> searchStocks(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("주식 검색 API 호출: keyword={}, page={}, size={}", keyword, page, size);

        PageResponse<StockResponse> stocks = stockService.searchStocks(keyword, page, size, sortBy, sortDir);

        String message = keyword != null ?
                String.format("'%s' 검색 결과", keyword) : "전체 주식 목록";

        return ApiResponse.success(message, stocks);
    }

    /**
     * 시장별 주식 조회
     */
    @GetMapping("/market/{marketCode}")
    public ApiResponse<PageResponse<StockResponse>> getStocksByMarket(
            @PathVariable String marketCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("시장별 주식 조회 API 호출: marketCode={}, page={}, size={}",
                marketCode, page, size);

        PageResponse<StockResponse> stocks = stockService.getStocksByMarket(marketCode, page, size, sortBy, sortDir);

        return ApiResponse.success(marketCode + " 시장 주식 조회 완료", stocks);
    }

    /**
     * 특정 주식 상세 조회
     */
    @GetMapping("/{stockId}")
    public ApiResponse<StockResponse> getStock(@PathVariable String stockId) {
        log.info("주식 상세 조회 API 호출: stockId={}", stockId);

        StockResponse stock = stockService.getStock(stockId);

        return ApiResponse.success("주식 상세 조회 완료", stock);
    }

    /**
     * 랜덤 주식 추천
     */
    @GetMapping("/random")
    public ApiResponse<List<StockResponse>> getRandomStocks(
            @RequestParam(defaultValue = "10") int count) {

        log.info("랜덤 주식 추천 API 호출: count={}", count);

        List<StockResponse> stocks = stockService.getRandomStocks(count);

        return ApiResponse.success("랜덤 주식 추천 완료", stocks);
    }

    /**
     * 주식 매수
     */
    @PostMapping("/buy")
    public ApiResponse<Void> buyStock(
            @RequestBody StockTradingRequest request,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            return ApiResponse.error("로그인이 필요합니다.");
        }

        log.info("매수 요청: loginId={}, stockId={}, quantity={}",
                loginUser.getLoginId(), request.getStockId(), request.getQuantity());

        userStockService.buyStock(loginUser.getLoginId(), request.getStockId(), request.getQuantity());

        return ApiResponse.success("매수가 완료되었습니다.", null);
    }

    /**
     * 주식 매도
     */
    @PostMapping("/sell")
    public ApiResponse<Void> sellStock(
            @RequestBody StockTradingRequest request,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            return ApiResponse.error("로그인이 필요합니다.");
        }

        log.info("매도 요청: loginId={}, stockId={}, quantity={}",
                loginUser.getLoginId(), request.getStockId(), request.getQuantity());

        userStockService.sellStock(loginUser.getLoginId(), request.getStockId(), request.getQuantity());

        return ApiResponse.success("매도가 완료되었습니다.", null);
    }

    /**
     * 거래량 랭킹 조회
     */
    @GetMapping("/rank")
    public ApiResponse<List<VolumeRankResponse>> getVolumeRanking() {
        log.info("거래량 랭킹 API 호출");

        List<VolumeRankResponse> rankings = volumeRankService.getVolumeRanking();

        if (rankings.isEmpty()) {
            return ApiResponse.success("현재 거래량 랭킹 데이터가 없습니다.", rankings);
        }

        return ApiResponse.success("거래량 랭킹 조회 완료", rankings);
    }
}