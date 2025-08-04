package personal.mockstock.domain.userStock.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.mockstock.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_stock")
public class UserStock {

    @Id
    @GeneratedValue
    @Column(name = "user_stock_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String stockId;          // 주식 코드
    private String stockName;        // 매수 당시 종목명
    private Integer averagePrice;    // 평균 매수가
    private Integer quantity;        // 보유 수량
    private Integer totalBuyAmount;  // 총 매수금액

    private LocalDateTime createdAt; // 최초 매수일
    private LocalDateTime updatedAt; // 마지막 거래일

    @Builder
    public UserStock(User user, String stockId, String stockName, Integer averagePrice, Integer quantity) {
        this.user = user;
        this.stockId = stockId;
        this.stockName = stockName;
        this.averagePrice = averagePrice;
        this.quantity = quantity;
        this.totalBuyAmount = averagePrice * quantity;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 추가 매수
     */
    public void addPurchase(Integer price, Integer addQuantity) {
        int newTotalQuantity = this.quantity + addQuantity;
        int newTotalAmount = this.totalBuyAmount + (price * addQuantity);

        this.averagePrice = newTotalAmount / newTotalQuantity; // 평균 단가 재계산
        this.quantity = newTotalQuantity;
        this.totalBuyAmount = newTotalAmount;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 일부 매도
     */
    public void sell(Integer sellQuantity) {
        this.quantity -= sellQuantity;
        this.totalBuyAmount = this.averagePrice * this.quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 현재 평가금액 계산 (외부에서 현재가를 받아서)
     */
    public Integer calculateCurrentValue(Integer currentPrice) {
        return currentPrice * this.quantity;
    }

    /**
     * 평가손익 계산
     */
    public Integer calculateProfitLoss(Integer currentPrice) {
        return calculateCurrentValue(currentPrice) - this.totalBuyAmount;
    }

    /**
     * 수익률 계산
     */
    public Double calculateReturnRate(Integer currentPrice) {
        if (this.totalBuyAmount == 0) return 0.0;
        return ((double) calculateProfitLoss(currentPrice) / this.totalBuyAmount) * 100;
    }
}