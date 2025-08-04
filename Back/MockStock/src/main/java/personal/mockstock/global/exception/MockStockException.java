package personal.mockstock.global.exception;

import lombok.Getter;

@Getter
public class MockStockException extends RuntimeException {

    private final ErrorCode errorCode;

    public MockStockException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public MockStockException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


}
