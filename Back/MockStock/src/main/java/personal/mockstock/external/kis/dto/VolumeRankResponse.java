package personal.mockstock.domain.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.mockstock.external.kis.dto.KisVolumeRankResponse;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolumeRankResponse {

    private Integer rank;               // 순위
    private String stockId;             // 종목코드
    private String stockName;           // 종목명
    private Integer currentPrice;       // 현재가
    private String priceChange;         // 전일대비
    private String changeRate;          // 전일대비율
    private Long totalVolume;           // 누적 거래량
    private Long totalTradeAmount;      // 누적 거래대금

    /**
     * KIS API 응답을 VolumeRankResponse로 변환
     */
    public static VolumeRankResponse from(KisVolumeRankResponse.VolumeRankItem item) {
        return VolumeRankResponse.builder()
                .rank(item.getRankAsInt())
                .stockId(item.getStockId())
                .stockName(item.getStockName())
                .currentPrice(item.getCurrentPriceAsInt())
                .priceChange(item.getPriceChange())
                .changeRate(item.getChangeRate())
                .totalVolume(item.getTotalVolumeAsLong())
                .totalTradeAmount(item.getTotalTradeAmountAsLong())
                .build();
    }

    /**
     * 거래량을 읽기 쉬운 형태로 포맷팅
     */
    public String getFormattedVolume() {
        if (totalVolume == null || totalVolume == 0) return "0";

        if (totalVolume >= 100_000_000) { // 1억 이상
            return String.format("%.1f억주", totalVolume / 100_000_000.0);
        } else if (totalVolume >= 10_000) { // 1만 이상
            return String.format("%.0f만주", totalVolume / 10_000.0);
        } else {
            return String.format("%,d주", totalVolume);
        }
    }

    /**
     * 거래대금을 읽기 쉬운 형태로 포맷팅
     */
    public String getFormattedTradeAmount() {
        if (totalTradeAmount == null || totalTradeAmount == 0) return "0원";

        if (totalTradeAmount >= 1_000_000_000_000L) { // 1조 이상
            return String.format("%.1f조원", totalTradeAmount / 1_000_000_000_000.0);
        } else if (totalTradeAmount >= 100_000_000L) { // 1억 이상
            return String.format("%.0f억원", totalTradeAmount / 100_000_000.0);
        } else if (totalTradeAmount >= 10_000L) { // 1만 이상
            return String.format("%.0f만원", totalTradeAmount / 10_000.0);
        } else {
            return String.format("%,d원", totalTradeAmount);
        }
    }

    /**
     * 거래량 기준 활발도 표시
     */
    public String getVolumeLevel() {
        if (totalVolume == null || totalVolume == 0) return "보통";

        if (totalVolume >= 50_000_000) return "매우활발"; // 5천만주 이상
        else if (totalVolume >= 10_000_000) return "활발";   // 1천만주 이상
        else if (totalVolume >= 1_000_000) return "보통";   // 100만주 이상
        else return "저조";
    }
}