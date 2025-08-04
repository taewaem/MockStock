package personal.mockstock.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.mockstock.domain.user.dto.UserCreateRequest;
import personal.mockstock.domain.user.dto.UserLoginRequest;
import personal.mockstock.domain.user.dto.UserRankingResponse;
import personal.mockstock.domain.user.dto.UserResponse;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.user.service.UserService;
import personal.mockstock.global.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest userCreateRequest) {

        log.info("회원 가입 요청 - loginId: {}", userCreateRequest.getLoginId());

        UserResponse userResponse = userService.createUser(userCreateRequest);
        ApiResponse<UserResponse> response = ApiResponse.success("회원가입이 완료되었습니다.", userResponse);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody UserLoginRequest loginRequest,
                                                           HttpServletRequest httpRequest) {

        log.info("로그인 요청 - loginId: {}", loginRequest.getLoginId());

        UserResponse userResponse = userService.login(loginRequest);

        HttpSession session = httpRequest.getSession();
        User user = userService.getUserEntityByLoginId(loginRequest.getLoginId());
        session.setAttribute("loginUser", user);


        ApiResponse<UserResponse> response = ApiResponse.success("로그인이 완료되었습니다.", userResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {

        log.info("로그아웃 요청");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        ApiResponse<Void> response = ApiResponse.success("로그아웃이 완료되었습니다.", null);

        return ResponseEntity.ok(response);
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            log.info("비로그인 사용자의 현재 사용자 정보 조회 시도");
            ApiResponse<UserResponse> response = ApiResponse.error("로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        UserResponse userResponse = userService.getUserByLoginId(loginUser.getLoginId());
        ApiResponse<UserResponse> response = ApiResponse.success(userResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 상세 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long userId) {

        log.info("사용자 상세 조회 API 호출: userId={}", userId);

        UserResponse userResponse = userService.getUserById(userId);
        ApiResponse<UserResponse> response = ApiResponse.success(userResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 랭킹 조회
     */
    @GetMapping("/users/rankings")
    public ResponseEntity<ApiResponse<List<UserRankingResponse>>> getTopUserRankings() {

        log.info("사용자 랭킹 조회 API 호출");

        List<UserRankingResponse> rankings = userService.getTopUserRankings();
        ApiResponse<List<UserRankingResponse>> response = ApiResponse.success(rankings);

        return ResponseEntity.ok(response);
    }

    /**
     * 전체 사용자 랭킹 조회
     */
    @GetMapping("/users/rankings/all")
    public ResponseEntity<ApiResponse<List<UserRankingResponse>>> getAllUserRankings() {

        log.info("전체 사용자 랭킹 조회 API 호출");

        List<UserRankingResponse> rankings = userService.getAllUserRankings();
        ApiResponse<List<UserRankingResponse>> response = ApiResponse.success(rankings);

        return ResponseEntity.ok(response);
    }

    /**
     * 파산 신청 (초기 자금으로 리셋)
     */
    @PostMapping("/bankruptcy")
    public ResponseEntity<ApiResponse<Void>> declareBankruptcy(
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<Void> response = ApiResponse.error("로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        log.info("파산 신청: loginId={}", loginUser.getLoginId());

        userService.processBankruptcy(loginUser.getLoginId());

        ApiResponse<Void> response = ApiResponse.success(
                "파산 처리가 완료되었습니다.", null);

        return ResponseEntity.ok(response);
    }

    /**
     * 인증 상태 확인 (React에서 사용)
     */
    @GetMapping("/auth/status")
    public ResponseEntity<ApiResponse<Boolean>> checkAuthStatus(
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        boolean isAuthenticated = loginUser != null;
        ApiResponse<Boolean> response = ApiResponse.success(isAuthenticated);

        return ResponseEntity.ok(response);
    }

}
