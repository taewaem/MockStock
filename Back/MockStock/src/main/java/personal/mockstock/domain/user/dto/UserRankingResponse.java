package personal.mockstock.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.mockstock.domain.user.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingResponse {

    private int rank;
    private String userName;
    private Long totalAssets;
    private Double returnRate;

    public static UserRankingResponse from(User user, int rank, Long seedMoney) {
        Long stockMoney = user.getStockMoney() != null ? user.getStockMoney() : 0L;
        Long totalAssets = user.getMoney() + stockMoney;
        Double returnRate = calculateReturnRate(totalAssets, seedMoney);

        return UserRankingResponse.builder()
                .rank(rank)
                .userName(user.getUserName())
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