package personal.mockstock.domain.stock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stock")
public class Stock {

    @Id
    @Column(name = "stock_id")
    private String stockId;
    private String stockName;
    private String marketCode;
    private Integer currentPrice;
    private String priceChange;
    private String changeRate;

    @Column(name = "price_updated_at")
    private LocalDateTime priceUpdatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Stock(String stockId, String stockName, String marketCode,
                 Integer currentPrice, String priceChange, String changeRate) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.marketCode = marketCode != null ? marketCode : "UNKNOWN";
        this.currentPrice = currentPrice != null ? currentPrice : 0;
        this.priceChange = priceChange != null ? priceChange : "0";
        this.changeRate = changeRate != null ? changeRate : "0.00%";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.priceUpdatedAt = LocalDateTime.now();
    }

    /**
     * 가격 정보 업데이트
     */
    public void updatePrice(Integer price, String priceChange, String changeRate) {
        this.currentPrice = price;
        this.priceChange = priceChange;
        this.changeRate = changeRate;
        this.priceUpdatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 종목명 업데이트
     */
    public void updateStockName(String stockName) {
        if (stockName != null && !stockName.equals(this.stockName)) {
            this.stockName = stockName;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * 거래 가능 여부 판단
     */
    public boolean isTradeable() {
        return currentPrice != null && currentPrice > 0;
    }

    /**
     * 매수 총 비용 계산
     */
    public Long calculateTotalCost(Integer quantity) {
        if (!isTradeable()) {
            throw new IllegalStateException("거래 불가능한 주식입니다: " + stockId);
        }
        return (long) currentPrice * quantity;
    }

}