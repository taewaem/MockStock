package Capstone.Third.api.kis.response;

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
public class KisVolumeRankRes {


    @JsonProperty("rt_cd")
    private String rtCd;    // 성공 실패 여부
    @JsonProperty("msg_cd")
    private String msg_cd;  // 응답코드
    @JsonProperty("msg1")
    private String msg1;    // 응답메세지
    @JsonProperty("output")
    private List<Output> output;  // 응답상세


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Builder
    public static class Output {
        @JsonProperty("hts_kor_isnm")
        private String hts_kor_isnm;  //한국 종목명
        @JsonProperty("mksc_shrn_iscd")
        private String mksc_shrn_iscd;    // 유가증권 단축 종목코드
        @JsonProperty("data_rank")
        private String data_rank;    // 데이터 순위
        @JsonProperty("stck_prpr")
        private String stck_prpr;    // 주식 현재가
        @JsonProperty("prdy_vrss")
        private String prdy_vrss;    // 전일 대비
        @JsonProperty("prdy_ctrt")
        private String prdy_ctrt;    // 전일 대비율
        @JsonProperty("acml_vol")
        private String acml_vol; // 누적 거래량
        @JsonProperty("prdy_vol")
        private String prdy_vol; // 전일 거래량
        @JsonProperty("avrg_vol")
        private String avrg_vol; // 평균 거래량
        @JsonProperty("acml_tr_pbmn")
        private String acml_tr_pbmn;  // 누적 거래 대금

    }



}
