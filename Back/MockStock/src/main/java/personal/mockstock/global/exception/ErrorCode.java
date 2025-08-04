package personal.mockstock.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 에러
    INVALID_INPUT_VALUE("C001", "잘못된 입력값입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("C002", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 인증 관련 에러
    UNAUTHORIZED("A001", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("A002", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 사용자 관련 에러
    USER_NOT_FOUND("U001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_LOGIN_ID("U002", "이미 존재하는 아이디입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("U003", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 주식 관련 에러
    STOCK_NOT_FOUND("S001", "주식 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE("S002", "잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("S003", "보유 주식이 부족합니다.", HttpStatus.BAD_REQUEST),
    INVALID_STOCK_COUNT("S004", "올바르지 않은 주식 수량입니다.", HttpStatus.BAD_REQUEST),

    //관심 목록 관련 에러
    INTEREST_NOT_FOUND("I001", "관심목록에 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 외부 API 관련 에러
    EXTERNAL_API_ERROR("E001", "외부 API 호출 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    KIS_API_ERROR("E002", "한국투자증권 API 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    KRX_API_ERROR("E003", "한국거래소 API 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

}
