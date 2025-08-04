// src/main/java/personal/mockstock/domain/user/controller/UserPortfolioController.java
package personal.mockstock.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.mockstock.domain.user.dto.UserPortfolioResponse;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.user.service.UserPortfolioService;
import personal.mockstock.global.dto.ApiResponse;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Slf4j
public class UserPortfolioController {

    private final UserPortfolioService userPortfolioService;

    /**
     * 내 포트폴리오 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<UserPortfolioResponse>> getMyPortfolio(
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<UserPortfolioResponse> response = ApiResponse.error("로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        log.info("포트폴리오 조회: loginId={}", loginUser.getLoginId());

        UserPortfolioResponse portfolio = userPortfolioService.getUserPortfolio(loginUser.getLoginId());
        ApiResponse<UserPortfolioResponse> response = ApiResponse.success("포트폴리오 조회 완료", portfolio);

        return ResponseEntity.ok(response);
    }
}