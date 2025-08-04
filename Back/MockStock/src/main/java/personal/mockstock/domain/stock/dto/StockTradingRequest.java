package personal.mockstock.domain.stock.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockTradingRequest {

    @NotBlank(message = "주식 코드를 입력해주세요")
    private String stockId;

    @NotNull(message = "거래 수량을 입력해주세요")
    @Min(value = 1, message = "거래 수량은 1 이상이어야 합니다")
    private Integer quantity;
}
