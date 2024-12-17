package Capstone.Third.api.kis.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드를 받는 생성자
public class StockInfoRes {

    @JsonProperty("rt_cd")
    private String resultCode;
    @JsonProperty("msg_cd")
    private String messageCode;
    @JsonProperty("output")
    private Output output;



    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Output {
        @JsonProperty("pdno")
        private String pdno;                    //주식 코드
        @JsonProperty("prdt_name")
        private String productName;            //주식 이름
        @JsonProperty("lstg_stqt")
        private String listedShares;            //상장 주수
        @JsonProperty("prdt_abrv_name")
        private String prdtAbrvName;
        @JsonProperty("std_idst_clsf_cd_name")
        private String stdIdstClsfCdName;       //산업 분류
        @JsonProperty("idx_bztp_mcls_cd_name")
        private String idxBztpMclsCdName;       //업종
        @JsonProperty("cpta")
        private String capitalAmount;           //자본금
    }
}