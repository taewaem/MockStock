package personal.mockstock.domain.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPortfolioResponse {

    private Long totalAssets;
    private Long availableCash;
    private Long stockValue;
    private Long totalProfitLoss;
    private Double totalReturnRate;

    private List<HoldingStock> holdings;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HoldingStock {
        private String stockId;
        private String stockName;
        private Integer quantity;
        private Integer averagePrice;
        private Integer currentPrice;
        private Integer totalValue;
        private Integer profitLoss;
        private Double returnRate;
    }
}
