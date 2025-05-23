package Capstone.Third.api.krx.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrxResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {

        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Header {
            @JsonProperty("resultCode")
            private String resultCode;

            @JsonProperty("resultMsg")
            private String resultMsg;
        }

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Body {

            @JsonProperty("numOfRows")
            private int numOfRows;

            @JsonProperty("pageNo")
            private int pageNo;

            @JsonProperty("totalCount")
            private int totalCount;

            @JsonProperty("items")
            private Items items;

            @Getter
            @NoArgsConstructor
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Items {
                @JsonProperty("item")
                private List<KrxItem> itemList;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KrxItem {

        @JsonProperty("srtnCd")
        private String srtnCd;      //주식 코드

        @JsonProperty("itmsNm")
        private String itmsNm;      //주식 이름

//        @JsonProperty("isinCd")
//        private String isinCd;
//
//        @JsonProperty("mrktCtg")
//        private String mrktCtg;
//
//        @JsonProperty("basDt")
//        private String basDt;
//
//        @JsonProperty("crno")
//        private String crno;
//
//        @JsonProperty("corpNm")
//        private String corpNm;
    }
}
