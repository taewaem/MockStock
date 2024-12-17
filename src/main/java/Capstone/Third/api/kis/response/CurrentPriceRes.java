package Capstone.Third.api.kis.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class CurrentPriceRes {

    @JsonProperty("output")
    private Output output;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    public static class Output {

        @JsonProperty("prdy_vrss")
        private String prdy_vrss;   //전일 대비
        @JsonProperty("prdy_ctrt")
        private String prdy_ctrt;   //전일 대비율
        @JsonProperty("acml_vol")
        private String acml_vol;    //누적 거래량
        @JsonProperty("hts_avls")
        private String hts_avls;    //시가총액
        @JsonProperty("w52_lwpr")
        private String w52_lwpr;    //52주 최저가
        @JsonProperty("w52_hgpr")
        private String w52_hgpr;    //52주 최고가
        @JsonProperty("per")
        private String per;         //PER
        @JsonProperty("pbr")
        private String pbr;         //PBR
        @JsonProperty("cpfn")
        private String cpfn;           //자본금
    }
}
