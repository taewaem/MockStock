package personal.mockstock.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.mockstock.domain.user.dto.UserCreateRequest;
import personal.mockstock.domain.user.dto.UserLoginRequest;
import personal.mockstock.domain.user.dto.UserRankingResponse;
import personal.mockstock.domain.user.dto.UserResponse;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.user.repository.UserRepository;
import personal.mockstock.domain.userStock.entity.UserStock;
import personal.mockstock.domain.userStock.repository.UserStockRepository;
import personal.mockstock.global.exception.ErrorCode;
import personal.mockstock.global.exception.MockStockException;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;

    // 시드머니 상수화
    private static final Long SEED_MONEY = 50000000L; // 5천만원

    /**
     * 회원가입
     */
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("회원가입 시도: loginId={}", request.getLoginId());

        userRepository.findByLoginId(request.getLoginId())
                .ifPresent(user -> {
                    throw new MockStockException(ErrorCode.DUPLICATE_LOGIN_ID);
                });

        User user = User.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .userName(request.getUserName())
                .money(SEED_MONEY)
                .build();

        user.updateStockMoney(0L); // 초기 주식 평가금액은 0

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: userId={}, loginId={}", savedUser.getId(), savedUser.getLoginId());

        return UserResponse.from(savedUser, SEED_MONEY);
    }

    /**
     * 로그인
     */
    public UserResponse login(UserLoginRequest request) {
        log.info("로그인 시도: loginId={}", request.getLoginId());

        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!user.getPassword().equals(request.getPassword())) {
            log.warn("비밀번호 불일치: loginId={}", request.getLoginId());
            throw new MockStockException(ErrorCode.INVALID_PASSWORD);
        }

        log.info("로그인 성공: userId={}, loginId={}", user.getId(), user.getLoginId());
        return UserResponse.from(user, SEED_MONEY);
    }

    /**
     * 사용자 상세 조회
     */
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user, SEED_MONEY);
    }

    /**
     * 로그인 ID로 사용자 조회
     */
    public UserResponse getUserByLoginId(String loginId) {
        User user = getUserEntityByLoginId(loginId);
        return UserResponse.from(user, SEED_MONEY);
    }

    /**
     * 사용자 랭킹 조회
     */
    public List<UserRankingResponse> getTopUserRankings() {
        List<User> users = userRepository.findAllOrderByTotalAssetsDesc();

        return IntStream.range(0, Math.min(5, users.size()))
                .mapToObj(i -> {
                    User user = users.get(i);
                    return UserRankingResponse.from(user, i + 1, SEED_MONEY); // SEED_MONEY 추가
                })
                .toList();
    }

    /**
     * 전체 사용자 랭킹 조회
     */
    public List<UserRankingResponse> getAllUserRankings() {
        List<User> users = userRepository.findAllOrderByTotalAssetsDesc();

        return IntStream.range(0, users.size())
                .mapToObj(i -> {
                    User user = users.get(i);
                    return UserRankingResponse.from(user, i + 1, SEED_MONEY);
                })
                .toList();
    }

    /**
     * 파산 처리
     */
    @Transactional
    public void processBankruptcy(String loginId) {
        log.info("파산 처리 시작: loginId={}", loginId);

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));

        List<UserStock> userStocks = userStockRepository.findByUser(user);
        if (!userStocks.isEmpty()) {
            userStockRepository.deleteAll(userStocks);
            log.info("보유 주식 전체 삭제 완료: loginId={}, 삭제된 종목 수={}",
                    loginId, userStocks.size());
        }

        user.updateMoney(SEED_MONEY);
        user.updateStockMoney(0L);
        userRepository.save(user);

        log.info("파산 처리 완료: loginId={}, 초기 자금={}원", loginId, SEED_MONEY);
    }

    /**
     * User 엔티티 반환
     */
    public User getUserEntityByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));
    }
}