package Capstone.Third.interest.repository;

import Capstone.Third.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    @Query("SELECT i FROM Interest i WHERE i.user.loginId = :loginId AND i.stock.stockId = :stockId")
    Interest findByLoginIdAndStockId(@Param("loginId") String loginId, @Param("stockId") String stockId);

    @Query("select i from Interest i where i.user.loginId = :loginId")
    List<Interest> findByLoginId(@Param("loginId") String loginId);


    @Query("select i from Interest i join i.stock s where s.id = :stockId")
    List<Interest> findByStockId(@Param("stockId") String stockId);

}
