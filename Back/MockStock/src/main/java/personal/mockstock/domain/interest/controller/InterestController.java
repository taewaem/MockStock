package personal.mockstock.domain.interest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.mockstock.domain.interest.dto.InterestRequest;
import personal.mockstock.domain.interest.dto.InterestResponse;
import personal.mockstock.domain.interest.service.InterestService;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.global.dto.ApiResponse;
import personal.mockstock.global.dto.PageResponse;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
@Slf4j
public class InterestController {

    private final InterestService interestService;

    /**
     * 관심목록 추가
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addInterest(
            @Valid @RequestBody InterestRequest request,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<Void> response = ApiResponse.error("로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        log.info("관심목록 추가 API 호출: loginId={}, stockId={}",
                loginUser.getLoginId(), request.getStockId());

        interestService.addInterest(loginUser.getLoginId(), request.getStockId());

        ApiResponse<Void> response = ApiResponse.success("관심목록에 추가되었습니다.", null);
        return ResponseEntity.ok(response);
    }

    /**
     * 관심목록 삭제
     */
    @DeleteMapping("/{stockId}")
    public ResponseEntity<ApiResponse<Void>> removeInterest(
            @PathVariable String stockId,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<Void> response = ApiResponse.error("로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        log.info("관심목록 삭제 API 호출: loginId={}, stockId={}",
                loginUser.getLoginId(), stockId);

        interestService.removeInterest(loginUser.getLoginId(), stockId);

        ApiResponse<Void> response = ApiResponse.success("관심목록에서 삭제되었습니다.", null);
        return ResponseEntity.ok(response);
    }

    /**
     * 내 관심목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<InterestResponse>>> getMyInterests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<PageResponse<InterestResponse>> response = ApiResponse.error("로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        log.info("내 관심목록 조회 API 호출: loginId={}, page={}, size={}",
                loginUser.getLoginId(), page, size);

        PageResponse<InterestResponse> interests = interestService.getMyInterests(
                loginUser.getLoginId(), page, size);

        ApiResponse<PageResponse<InterestResponse>> response =
                ApiResponse.success("관심목록 조회 완료", interests);
        return ResponseEntity.ok(response);
    }

    /**
     * 관심목록 검색 (페이징)
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<InterestResponse>>> searchMyInterests(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<PageResponse<InterestResponse>> response = ApiResponse.error("로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }

        log.info("관심목록 검색 API 호출: loginId={}, keyword={}, page={}, size={}",
                loginUser.getLoginId(), keyword, page, size);

        PageResponse<InterestResponse> interests = interestService.searchMyInterests(
                loginUser.getLoginId(), keyword, page, size);

        ApiResponse<PageResponse<InterestResponse>> response =
                ApiResponse.success("관심목록 검색 완료", interests);
        return ResponseEntity.ok(response);
    }

    /**
     * 관심목록 여부 확인
     */
    @GetMapping("/check/{stockId}")
    public ResponseEntity<ApiResponse<Boolean>> checkInterest(
            @PathVariable String stockId,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<Boolean> response = ApiResponse.success(false);
            return ResponseEntity.ok(response);
        }

        log.info("관심목록 여부 확인 API 호출: loginId={}, stockId={}",
                loginUser.getLoginId(), stockId);

        boolean isInterested = interestService.isInterested(loginUser.getLoginId(), stockId);

        ApiResponse<Boolean> response = ApiResponse.success(isInterested);
        return ResponseEntity.ok(response);
    }

    /**
     * 내 관심목록 개수 조회
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getMyInterestCount(
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            ApiResponse<Long> response = ApiResponse.success(0L);
            return ResponseEntity.ok(response);
        }

        log.info("관심목록 개수 조회 API 호출: loginId={}", loginUser.getLoginId());

        long count = interestService.getMyInterestCount(loginUser.getLoginId());

        ApiResponse<Long> response = ApiResponse.success("관심목록 개수 조회 완료", count);
        return ResponseEntity.ok(response);
    }

}