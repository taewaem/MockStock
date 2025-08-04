package personal.mockstock.external.kis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class KisVolumeRankResponse {

    @JsonProperty("rt_cd")
    private String rtCd;        // 성공 실패 여부

    @JsonProperty("msg_cd")
    private String msgCd;       // 응답코드

    @JsonProperty("msg1")
    private String msg1;        // 응답메세지

    @JsonProperty("output")
    private List<VolumeRankItem> output;  // 거래량 랭킹 데이터

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VolumeRankItem {

        @JsonProperty("hts_kor_isnm")
        private String stockName;           // 한국 종목명

        @JsonProperty("mksc_shrn_iscd")
        private String stockId;             // 유가증권 단축 종목코드

        @JsonProperty("data_rank")
        private String rank;                // 데이터 순위

        @JsonProperty("stck_prpr")
        private String currentPrice;        // 주식 현재가

        @JsonProperty("prdy_vrss")
        private String priceChange;         // 전일 대비

        @JsonProperty("prdy_ctrt")
        private String changeRate;          // 전일 대비율

        @JsonProperty("acml_vol")
        private String totalVolume;         // 누적 거래량

        @JsonProperty("prdy_vol")
        private String prevDayVolume;       // 전일 거래량

        @JsonProperty("avrg_vol")
        private String avgVolume;           // 평균 거래량

        @JsonProperty("acml_tr_pbmn")
        private String totalTradeAmount;    // 누적 거래 대금

        /**
         * 안전한 정수 변환
         */
        public Integer getCurrentPriceAsInt() {
            return parseStringToInt(currentPrice);
        }

        public Integer getRankAsInt() {
            return parseStringToInt(rank);
        }

        public Long getTotalVolumeAsLong() {
            return parseStringToLong(totalVolume);
        }

        public Long getTotalTradeAmountAsLong() {
            return parseStringToLong(totalTradeAmount);
        }

        private Integer parseStringToInt(String value) {
            if (value == null || value.trim().isEmpty()) return 0;
            try {
                return Integer.valueOf(value.replace(",", ""));
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        private Long parseStringToLong(String value) {
            if (value == null || value.trim().isEmpty()) return 0L;
            try {
                return Long.valueOf(value.replace(",", ""));
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
    }

    /**
     * API 응답 성공 여부 확인
     */
    public boolean isSuccess() {
        return "0".equals(rtCd);
    }

    /**
     * 유효한 데이터 여부 확인
     */
    public boolean hasValidData() {
        return output != null && !output.isEmpty();
    }
}