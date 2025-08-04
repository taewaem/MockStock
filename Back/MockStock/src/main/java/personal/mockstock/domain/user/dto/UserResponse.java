package personal.mockstock.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.mockstock.domain.user.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String loginId;
    private String userName;
    private Long availableMoney;
    private Long stockMoney;
    private Long totalAssets;
    private Double returnRate;

        public static UserResponse from(User user, Long seedMoney) {
        Long stockMoney = user.getStockMoney() != null ? user.getStockMoney() : 0L;
        Long totalAssets = user.getMoney() + stockMoney;
        Double returnRate = calculateReturnRate(totalAssets, seedMoney);

        return UserResponse.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .userName(user.getUserName())
                .availableMoney(user.getMoney())
                .stockMoney(stockMoney)
                .totalAssets(totalAssets)
                .returnRate(returnRate)
                .build();
    }

    private static Double calculateReturnRate(Long totalAssets, Long seedMoney) {
        if (seedMoney == null || seedMoney == 0) {
            return 0.0;
        }
        return ((double)(totalAssets - seedMoney) / seedMoney) * 100;
    }
}