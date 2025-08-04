package personal.mockstock.domain.userStock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.mockstock.domain.user.entity.User;
import personal.mockstock.domain.userStock.entity.UserStock;

import java.util.List;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    List<UserStock> findByUser(User user);

    UserStock findByUserAndStockId(User user, String stockId);

    List<UserStock> findByUserId(Long userId);
}
