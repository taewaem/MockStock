package personal.mockstock.domain.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import personal.mockstock.domain.stock.dto.VolumeRankResponse;
import personal.mockstock.external.kis.dto.KisVolumeRankResponse;
import personal.mockstock.external.kis.service.KisApiService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VolumeRankService {

    private final KisApiService kisApiService;

    /**
     * 실시간 거래량 랭킹 조회 (상위 30개)
     */
    public List<VolumeRankResponse> getVolumeRanking() {
        log.info("거래량 랭킹 조회 요청");

        try {
            // KIS API 호출
            KisVolumeRankResponse kisResponse = kisApiService.getVolumeRanking();

            // 응답 유효성 검사
            if (!kisApiService.isValidResponse(kisResponse)) {
                log.warn("KIS API 응답이 유효하지 않습니다.");
                return Collections.emptyList();
            }

            // DTO 변환
            List<VolumeRankResponse> rankings = kisResponse.getOutput().stream()
                    .limit(10)
                    .map(VolumeRankResponse::from)
                    .toList();

            log.info("거래량 랭킹 조회 완료: {}개", rankings.size());
            return rankings;

        } catch (Exception e) {
            log.error("거래량 랭킹 조회 중 오류 발생", e);
            return Collections.emptyList();
        }
    }

    /**
     * 거래량 랭킹 상위 N개 조회
     */
    public List<VolumeRankResponse> getTopVolumeRanking(int count) {
        log.info("거래량 랭킹 상위 {}개 조회 요청", count);

        List<VolumeRankResponse> allRankings = getVolumeRanking();

        return allRankings.stream()
                .limit(Math.max(1, Math.min(count, 30))) // 1~30 범위로 제한
                .toList();
    }

    /**
     * 특정 종목의 거래량 순위 확인
     */
    public Integer getStockVolumeRank(String stockId) {
        log.info("종목 거래량 순위 조회: stockId={}", stockId);

        try {
            List<VolumeRankResponse> rankings = getVolumeRanking();

            for (int i = 0; i < rankings.size(); i++) {
                if (stockId.equals(rankings.get(i).getStockId())) {
                    int rank = i + 1;
                    log.info("종목 거래량 순위 조회 완료: stockId={}, rank={}", stockId, rank);
                    return rank;
                }
            }

            log.info("거래량 랭킹 30위 밖: stockId={}", stockId);
            return null; // 30위 밖

        } catch (Exception e) {
            log.error("종목 거래량 순위 조회 중 오류 발생: stockId={}", stockId, e);
            return null;
        }
    }

    /**
     * 오늘의 거래량 왕 (1위)
     */
    public VolumeRankResponse getTodayVolumeKing() {
        log.info("오늘의 거래량 왕 조회");

        List<VolumeRankResponse> rankings = getTopVolumeRanking(1);

        if (!rankings.isEmpty()) {
            VolumeRankResponse king = rankings.get(0);
            log.info("오늘의 거래량 왕: {} ({})", king.getStockName(), king.getFormattedVolume());
            return king;
        }

        log.warn("거래량 랭킹 데이터가 없습니다.");
        return null;
    }
}