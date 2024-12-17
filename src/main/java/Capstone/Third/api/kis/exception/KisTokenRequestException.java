package Capstone.Third.api.kis.exception;

public class KisTokenRequestException extends RuntimeException {

    public KisTokenRequestException(String 토큰_발급_실패, Exception e){
        super("토큰 발생 중 오류가 발생하였습니다.");
    }
}
