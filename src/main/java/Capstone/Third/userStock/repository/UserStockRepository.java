package Capstone.Third.userStock.repository;

import Capstone.Third.userStock.entity.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {



    @Query("SELECT u FROM UserStock u WHERE u.user.loginId = :loginId AND u.stock.stockId = :stockId")
    UserStock findByLoginIdAndStockId(@Param("loginId") String loginId, @Param("stockId") String stockId);

    @Query("select u from UserStock u where u.user.loginId = :loginId")
    List<UserStock> findByLoginId(@Param("loginId") String loginId);
}
