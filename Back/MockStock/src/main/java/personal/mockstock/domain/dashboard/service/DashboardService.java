package personal.mockstock.domain.dashboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.mockstock.domain.dashboard.dto.DashboardResponse;
import personal.mockstock.domain.stock.dto.StockResponse;
import personal.mockstock.domain.stock.dto.VolumeRankResponse;
import personal.mockstock.domain.stock.service.StockService;
import personal.mockstock.domain.stock.service.VolumeRankService;
import personal.mockstock.domain.user.dto.UserRankingResponse;
import personal.mockstock.domain.user.dto.UserResponse;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final UserService userService;
    private final StockService stockService;
    private final VolumeRankService volumeRankService;

    public DashboardResponse getDashboardData(User loginUser) {
        log.info("대시보드 데이터 조회 시작");

        try {
            List<UserRankingResponse> topUsers = userService.getTopUserRankings();
            log.info("사용자 랭킹 조회 완료: {}명", topUsers.size());

            List<StockResponse> recommendedStocks = stockService.getRandomStocks(10);
            log.info("추천 주식 조회 완료: {}개", recommendedStocks.size());

            List<VolumeRankResponse> volumeRankings = volumeRankService.getTopVolumeRanking(10);
            log.info("거래량 랭킹 조회 완료: {}개", volumeRankings.size());

            UserResponse currentUser = null;
            if (loginUser    != null) {
                currentUser = userService.getUserByLoginId(loginUser.getLoginId());
                log.info("로그인 사용자 정보 조회 완료: {}", loginUser.getLoginId());
            } else {
                log.info("비로그인 사용자 대시보드 요청");
            }

            DashboardResponse response = DashboardResponse.builder()
                    .isAuthenticated(loginUser != null)
                    .currentUser(currentUser)
                    .topUsers(topUsers)
                    .recommendedStocks(recommendedStocks)
                    .volumeRankings(volumeRankings)
                    .build();

            log.info("대시보드 데이터 조회 완료");
            return response;

        } catch (Exception e) {
            log.error("대시보드 데이터 조회 중 오류 발생", e);

            // 오류 발생 시 빈 데이터 반환
            return DashboardResponse.builder()
                    .isAuthenticated(loginUser != null)
                    .currentUser(loginUser != null ? userService.getUserByLoginId(loginUser.getLoginId()) : null)
                    .topUsers(List.of())
                    .recommendedStocks(List.of())
                    .volumeRankings(List.of())
                    .build();
        }
    }
}   