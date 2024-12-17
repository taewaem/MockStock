package Capstone.Third.api.kis.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Slf4j
@AllArgsConstructor
public class ClosingPriceRes {


    @JsonProperty("output")
    private List<Output> output;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    public static class Output {

        @JsonProperty("stck_clpr")
        private String stck_clpr;       //주식 종가
        @JsonProperty("stck_bsop_date")
        private String stck_bsop_date;  //날짜
        @JsonProperty("prdy_vrss")
        private String prdy_vrss;       //전일 대비
        @JsonProperty("prdy_ctrt")
        private String prdy_ctrt;       //전일 대비율

    }
}
