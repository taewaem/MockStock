package personal.mockstock.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.mockstock.domain.stock.entity.Stock;
import personal.mockstock.domain.stock.repository.StockRepository;
import personal.mockstock.domain.stock.service.StockService;
import personal.mockstock.domain.user.dto.UserPortfolioResponse;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.user.repository.UserRepository;
import personal.mockstock.domain.userStock.entity.UserStock;
import personal.mockstock.domain.userStock.repository.UserStockRepository;
import personal.mockstock.global.exception.ErrorCode;
import personal.mockstock.global.exception.MockStockException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPortfolioService {

    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;
    private final StockRepository stockRepository;

    public UserPortfolioResponse getUserPortfolio(String loginId) {

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));

        List<UserStock> userStockList = userStockRepository.findByUser(user);

        List<UserPortfolioResponse.HoldingStock> hodlingStockList = userStockList.stream()
                .map(userStock -> {

                    try {
                        Stock stock = stockRepository.findById(userStock.getStockId())
                                .orElseThrow(() -> new MockStockException(ErrorCode.STOCK_NOT_FOUND));

                        Integer currentPrice = stock.getCurrentPrice();

                        return UserPortfolioResponse.HoldingStock.builder()
                                .stockId(userStock.getStockId())
                                .stockName(userStock.getStockName())
                                .quantity(userStock.getQuantity())
                                .averagePrice(userStock.getAveragePrice())
                                .currentPrice(currentPrice)
                                .totalValue(userStock.calculateCurrentValue(currentPrice))
                                .profitLoss(userStock.calculateProfitLoss(currentPrice))
                                .returnRate(userStock.calculateReturnRate(currentPrice))
                                .build();

                    } catch (Exception e) {

                        log.error("주식 현재가 조회 실패: stockId={}", userStock.getStockId(), e);

                        return UserPortfolioResponse.HoldingStock.builder()
                                .stockId(userStock.getStockId())
                                .stockName(userStock.getStockName())
                                .quantity(userStock.getQuantity())
                                .averagePrice(userStock.getAveragePrice())
                                .currentPrice(userStock.getAveragePrice()) // 평균가로 대체
                                .totalValue(userStock.getTotalBuyAmount())
                                .profitLoss(0)
                                .returnRate(0.0)
                                .build();
                    }
                })
                .toList();

        Long totalStockValue = hodlingStockList.stream()
                .mapToLong(holding -> holding.getTotalValue().longValue())
                .sum();

        Long totalProfitLoss = hodlingStockList.stream()
                .mapToLong(holding -> holding.getProfitLoss().longValue())
                .sum();

        Long totalAssets = user.getMoney() + totalStockValue;

        // 시드머니 기준 수익률 계산
        Long seedMoney = 50000000L;
        Double totalReturnRate = ((double)(totalAssets - seedMoney) / seedMoney) * 100;

        return UserPortfolioResponse.builder()
                .totalAssets(totalAssets)
                .availableCash(user.getMoney())
                .stockValue(totalStockValue)
                .totalProfitLoss(totalProfitLoss)
                .totalReturnRate(totalReturnRate)
                .holdings(hodlingStockList)
                .build();
    }


}
