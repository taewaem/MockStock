package personal.mockstock.domain.interest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.mockstock.domain.interest.dto.InterestResponse;
import personal.mockstock.domain.interest.entity.Interest;
import personal.mockstock.domain.interest.repository.InterestRepository;
import personal.mockstock.domain.stock.entity.Stock;
import personal.mockstock.domain.stock.repository.StockRepository;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.user.repository.UserRepository;
import personal.mockstock.domain.user.service.UserService;
import personal.mockstock.global.dto.PageResponse;
import personal.mockstock.global.exception.ErrorCode;
import personal.mockstock.global.exception.MockStockException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InterestService {

    private final InterestRepository interestRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    /**
     * 관심목록 추가
     */
    @Transactional
    public void addInterest(String loginId, String stockId) {
        log.info("관심목록 추가 요청: loginId={}, stockId={}", loginId, stockId);

        User user = getUser(loginId);

        Stock stock = getStock(stockId);

        if (interestRepository.existsByUserAndStock(user, stock)) {
            log.warn("이미 관심목록에 추가된 주식: loginId={}, stockId={}", loginId, stockId);
            throw new MockStockException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Interest interest = Interest.builder()
                .user(user)
                .stock(stock)
                .build();

        interestRepository.save(interest);

        log.info("관심목록 추가 완료: loginId={}, stockId={}, stockName={}",
                loginId, stockId, stock.getStockName());
    }

    /**
     * 관심목록 삭제
     */
    @Transactional
    public void removeInterest(String loginId, String stockId) {
        log.info("관심목록 삭제 요청: loginId={}, stockId={}", loginId, stockId);

        User user = getUser(loginId);

        Stock stock = getStock(stockId);

        Interest interest = interestRepository.findByUserAndStock(user, stock)
                .orElseThrow(() -> new MockStockException(ErrorCode.INTEREST_NOT_FOUND));

        interestRepository.delete(interest);

        log.info("관심목록 삭제 완료: loginId={}, stockId={}", loginId, stockId);
    }

    /**
     * 내 관심목록 조회
     */
    public PageResponse<InterestResponse> getMyInterests(String loginId, int page, int size) {
        log.info("관심목록 조회: loginId={}, page={}, size={}", loginId, page, size);

        User user = getUser(loginId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Interest> interestPage = interestRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        List<InterestResponse> content = interestPage.getContent().stream()
                .map(InterestResponse::from)
                .toList();

        log.info("관심목록 조회 완료: loginId={}, 조회된 개수={}", loginId, content.size());

        return PageResponse.from(interestPage, content);
    }

    /**
     * 관심목록 검색 (페이징)
     */
    public PageResponse<InterestResponse> searchMyInterests(String loginId, String keyword, int page, int size) {
        log.info("관심목록 검색: loginId={}, keyword={}, page={}, size={}", loginId, keyword, page, size);

        User user = getUser(loginId);

        Pageable pageable = PageRequest.of(page, size);

        Page<Interest> interestPage = interestRepository.findByUserAndStockNameContaining(user, keyword, pageable);

        List<InterestResponse> content = interestPage.getContent().stream()
                .map(InterestResponse::from)
                .toList();

        return PageResponse.from(interestPage, content);
    }

    /**
     * 관심목록 여부 확인
     */
    public boolean isInterested(String loginId, String stockId) {
        try {
            User user = getUser(loginId);

            Stock stock = stockRepository.findById(stockId).orElse(null);

            if (stock == null) return false;

            return interestRepository.existsByUserAndStock(user, stock);
        } catch (Exception e) {
            log.warn("관심목록 여부 확인 실패: loginId={}, stockId={}", loginId, stockId);
            return false;
        }
    }

    /**
     * 내 관심목록 개수 조회
     */
    public long getMyInterestCount(String loginId) {
        User user = getUser(loginId);
        return interestRepository.countByUser(user);
    }

    private User getUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MockStockException(ErrorCode.USER_NOT_FOUND));
    }

    private Stock getStock(String stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new MockStockException(ErrorCode.STOCK_NOT_FOUND));
    }
}