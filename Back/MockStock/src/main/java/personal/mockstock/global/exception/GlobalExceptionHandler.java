package personal.mockstock.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import personal.mockstock.global.dto.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MockStockException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(MockStockException e) {
        log.error("MockStockException: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.error(e.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }


    /**
     * 예상치 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected Exception: ", e);

        ApiResponse<Void> response = ApiResponse.error("서버 내부 오류가 발생했습니다.");
        return ResponseEntity.internalServerError().body(response);
    }
}
