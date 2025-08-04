package personal.mockstock.domain.stock.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.mockstock.domain.stock.entity.Stock;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {

    private String stockId;
    private String stockName;
    private Integer currentPrice;
    private String priceChange;
    private String changeRate;
    private String marketCode;

    /**
     * 거래 가능 여부 확인
     */
    public boolean isTradeable() {
        return currentPrice != null && currentPrice > 0;
    }
}