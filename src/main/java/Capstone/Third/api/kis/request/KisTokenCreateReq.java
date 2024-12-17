package Capstone.Third.api.kis.request;

import Capstone.Third.api.kis.config.KisConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KisTokenCreateReq {

    @JsonProperty("grant_type")
    private String grantType = "client_credentials";
    @JsonProperty("appkey")
    private String appKey;
    @JsonProperty("appsecret")
    private String appSecret;

    public KisTokenCreateReq(KisConfig kisConfig) {
        this.appKey = kisConfig.getAppKey();
        this.appSecret = kisConfig.getAppSecret();
    }
}
