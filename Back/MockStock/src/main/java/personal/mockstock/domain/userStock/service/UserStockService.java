package personal.mockstock.domain.userStock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.mockstock.domain.stock.entity.Stock;
import personal.mockstock.domain.stock.repository.StockRepository;
import personal.mockstock.domain.stock.service.StockService;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.user.repository.UserRepository;
import personal.mockstock.domain.userStock.entity.UserStock;
import personal.mockstock.domain.userStock.repository.UserStockRepository;
import personal.mockstock.global.exception.ErrorCode;
import personal.mockstock.global.exception.MockStockException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserStockService {

    private final UserStockRepository userStockRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    /**
     * 주식 매수
     */
    @Transactional
    public void buyStock(String loginId, String stockId, Integer quantity) {
        log.info("주식 매수 요청: loginId={}, stockId={}, quantity={}", loginId, stockId, quantity);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new MockStockException(ErrorCode.STOCK_NOT_FOUND));

        Integer currentPrice = stock.getCurrentPrice();

        Long totalCost = (long) currentPrice * quantity;

        if (user.getMoney() < totalCost) {
            throw new MockStockException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        UserStock existingStock = userStockRepository.findByUserAndStockId(user, stockId);

        if (existingStock != null) {
            // 추가 매수
            existingStock.addPurchase(currentPrice, quantity);
        } else {
            // 신규 매수
            String stockName = stock.getStockName();
            existingStock = UserStock.builder()
                    .user(user)
                    .stockId(stockId)
                    .stockName(stockName)
                    .averagePrice(currentPrice)
                    .quantity(quantity)
                    .build();
        }

        // 6. 저장 및 사용자 자액 업데이트
        userStockRepository.save(existingStock);
        user.updateMoney(user.getMoney() - totalCost);

        // 7. 주식 평가액 업데이트
        updateUserStockValue(user);

        log.info("매수 완료: stockId={}, price={}, quantity={}, totalCost={}",
                stockId, currentPrice, quantity, totalCost);
    }

    /**
     * 주식 매도 (24시간 언제든 가능)
     */
    @Transactional
    public void sellStock(String loginId, String stockId, Integer quantity) {
        log.info("주식 매도 요청: loginId={}, stockId={}, quantity={}", loginId, stockId, quantity);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));

        // 2. 보유 주식 확인
        UserStock userStock = userStockRepository.findByUserAndStockId(user, stockId);
        if (userStock == null) {
            throw new MockStockException(ErrorCode.INSUFFICIENT_STOCK, "보유하지 않은 주식입니다.");
        }

        if (userStock.getQuantity() < quantity) {
            throw new MockStockException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // 3. 현재 종가 조회
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new MockStockException(ErrorCode.STOCK_NOT_FOUND));

        Integer currentPrice = stock.getCurrentPrice();

        Long sellAmount = (long) currentPrice * quantity;

        // 4. 매도 처리
        if (userStock.getQuantity().equals(quantity)) {
            // 전량 매도 - 삭제
            userStockRepository.delete(userStock);
        } else {
            // 일부 매도
            userStock.sell(quantity);
            userStockRepository.save(userStock);
        }

        // 5. 사용자 현금 및 주식 평가액 업데이트
        user.updateMoney(user.getMoney() + sellAmount);
        updateUserStockValue(user);

        log.info("매도 완료: stockId={}, price={}, quantity={}, sellAmount={}",
                stockId, currentPrice, quantity, sellAmount);
    }

    /**
     * 사용자 주식 평가액 업데이트 (내부 메서드)
     */
    private void updateUserStockValue(User user) {
        List<UserStock> userStocks = userStockRepository.findByUser(user);

        Long totalStockValue = userStocks.stream()
                .mapToLong(userStock -> {
                    try {
                        Stock stock = stockRepository.findById(userStock.getStockId())
                                .orElseThrow(() -> new MockStockException(ErrorCode.STOCK_NOT_FOUND));

                        Integer currentPrice = stock.getCurrentPrice();
                        return (long) currentPrice * userStock.getQuantity();
                    } catch (Exception e) {
                        log.warn("주식 현재가 조회 실패: stockId={}, 평균가로 계산", userStock.getStockId());
                        return (long) userStock.getAveragePrice() * userStock.getQuantity();
                    }
                })
                .sum();

        user.updateStockMoney(totalStockValue);
        log.debug("사용자 주식 평가액 업데이트: loginId={}, stockValue={}", user.getLoginId(), totalStockValue);
    }

    /**
     * 전체 사용자 주식 평가액 일괄 업데이트 (스케줄러용)
     */
    @Transactional
    public void updateAllUsersStockValue() {
        log.info("전체 사용자 주식 평가액 업데이트 시작");

        List<User> users = userRepository.findAll();

        for (User user : users) {
            try {
                updateUserStockValue(user);
            } catch (Exception e) {
                log.error("사용자 주식 평가액 업데이트 실패: loginId={}", user.getLoginId(), e);
            }
        }

        log.info("전체 사용자 주식 평가액 업데이트 완료: {}명", users.size());
    }
}