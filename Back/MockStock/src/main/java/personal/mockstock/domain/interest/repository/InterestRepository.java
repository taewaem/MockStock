package personal.mockstock.domain.interest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.mockstock.domain.interest.entity.Interest;
import personal.mockstock.domain.stock.entity.Stock;
import personal.mockstock.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    /**
     * 특정 사용자의 특정 주식 관심목록 조회
     */
    @Query("SELECT i FROM Interest i WHERE i.user = :user AND i.stock = :stock")
    Optional<Interest> findByUserAndStock(@Param("user") User user, @Param("stock") Stock stock);

    /**
     * 특정 사용자의 전체 관심목록 조회
     */
    @Query("SELECT i FROM Interest i WHERE i.user = :user ORDER BY i.createdAt DESC")
    List<Interest> findByUserOrderByCreatedAtDesc(@Param("user") User user);

    /**
     * 특정 사용자의 관심목록 페이징 조회
     */
    @Query("SELECT i FROM Interest i WHERE i.user = :user ORDER BY i.createdAt DESC")
    Page<Interest> findByUserOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);

    /**
     * 특정 사용자의 관심목록 개수
     */
    @Query("SELECT COUNT(i) FROM Interest i WHERE i.user = :user")
    long countByUser(@Param("user") User user);

    /**
     * 사용자가 특정 주식을 관심목록에 추가했는지 확인
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Interest i WHERE i.user = :user AND i.stock = :stock")
    boolean existsByUserAndStock(@Param("user") User user, @Param("stock") Stock stock);

    /**
     * 특정 사용자의 관심목록에서 종목명으로 검색
     */
    @Query("SELECT i FROM Interest i WHERE i.user = :user AND i.stock.stockName LIKE %:keyword% ORDER BY i.createdAt DESC")
    Page<Interest> findByUserAndStockNameContaining(@Param("user") User user,
                                                    @Param("keyword") String keyword,
                                                    Pageable pageable);
}