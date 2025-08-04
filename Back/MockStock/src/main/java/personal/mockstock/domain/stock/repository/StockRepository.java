package personal.mockstock.domain.stock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.mockstock.domain.stock.entity.Stock;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {

    // 종목명 페이징 검색
    @Query("SELECT s FROM Stock s WHERE s.stockName LIKE %:stockName%")
    Page<Stock> findByStockName(@Param("stockName") String stockName, Pageable pageable);

    // 종목명이나 코드로 검색
    @Query("SELECT s FROM Stock s WHERE s.stockName LIKE %:keyword% OR s.stockId LIKE %:keyword%")
    Page<Stock> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 시장별 조회
    @Query("SELECT s FROM Stock s WHERE s.marketCode = :marketCode")
    Page<Stock> findByMarketCode(@Param("marketCode") String marketCode, Pageable pageable);

//    /**
//     * 현재가가 있는 주식만 페이징 조회
//     */
//    @Query("SELECT s FROM Stock s WHERE s.currentPrice IS NOT NULL AND s.currentPrice > 0")
//    Page<Stock> findByCurrentPriceIsNotNull(Pageable pageable);

//    /**
//     * 시장별 + 검색어
//     */
//    @Query("SELECT s FROM Stock s WHERE s.marketCode = :marketCode AND " +
//            "(s.stockName LIKE %:keyword% OR s.stockId LIKE %:keyword%)")
//    Page<Stock> findByMarketCodeAndKeyword(@Param("marketCode") String marketCode,
//                                           @Param("keyword") String keyword,
//                                           Pageable pageable);


    // 시장별 조회
//    @Query("SELECT s FROM Stock s WHERE s.marketCode = :marketCode")
//    List<Stock> findByMarketCodeList(@Param("marketCode") String marketCode);

}