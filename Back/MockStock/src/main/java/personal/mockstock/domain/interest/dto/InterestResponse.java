package personal.mockstock.domain.interest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.mockstock.domain.interest.entity.Interest;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterestResponse {

    private Long interestId;
    private String stockId;
    private String stockName;
    private Integer currentPrice;    // 현재종가
    private String priceChange;      // 전일대비
    private String changeRate;       // 전일대비율
    private String marketCode;       // 시장구분

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime addedAt;

    public static InterestResponse from(Interest interest) {
        return InterestResponse.builder()
                .interestId(interest.getId())
                .stockId(interest.getStock().getStockId())
                .stockName(interest.getStock().getStockName())
                .currentPrice(interest.getStock().getCurrentPrice())
                .priceChange(interest.getStock().getPriceChange())
                .changeRate(interest.getStock().getChangeRate())
                .marketCode(interest.getStock().getMarketCode())
                .addedAt(interest.getStock().getCreatedAt())
                .build();
    }


}