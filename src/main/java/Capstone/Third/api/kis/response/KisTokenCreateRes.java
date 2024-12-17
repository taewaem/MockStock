package Capstone.Third.api.kis.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * ignoreUnknown = true
 * -> 역직렬화, JSON 데이터가 가진 프로퍼티 중에 자바 class의 vo 프로퍼티에 값이 없는경우에러를 던지지 않고 무시됩니다.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class KisTokenCreateRes {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private String expiresIn;
    @JsonProperty("access_token_token_expired")
    private String accessTokenTokenExpired;
}
