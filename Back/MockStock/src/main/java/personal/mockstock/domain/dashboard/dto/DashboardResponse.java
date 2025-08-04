package personal.mockstock.domain.dashboard.dto;

import lombok.*;
import personal.mockstock.domain.stock.dto.StockResponse;
import personal.mockstock.domain.stock.dto.VolumeRankResponse;
import personal.mockstock.domain.user.dto.UserRankingResponse;
import personal.mockstock.domain.user.dto.UserResponse;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // 인증 상태
    private boolean isAuthenticated;

    private UserResponse currentUser;

    private List<UserRankingResponse> topUsers;

    private List<StockResponse> recommendedStocks;

    private List<VolumeRankResponse> volumeRankings;
}