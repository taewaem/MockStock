package personal.mockstock.domain.dashboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import personal.mockstock.domain.dashboard.dto.DashboardResponse;
import personal.mockstock.domain.dashboard.service.DashboardService;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.global.dto.ApiResponse;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardApiController {

    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard(
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        log.info("대시보드 API 호출");

        DashboardResponse dashboard = dashboardService.getDashboardData(loginUser);
        return ApiResponse.success("대시보드 조회 완료", dashboard);
    }

}
