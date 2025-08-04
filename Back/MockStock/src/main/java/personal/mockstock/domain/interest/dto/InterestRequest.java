package personal.mockstock.domain.interest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterestRequest {

    @NotBlank(message = "주식 코드를 입력해주세요")
    private String stockId;

    /**
     * 관심목록 추가 요청 생성
     */
    public static InterestRequest of(String stockId) {
        return new InterestRequest(stockId);
    }
}