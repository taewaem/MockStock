package personal.mockstock.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.mockstock.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    @Query("SELECT u FROM User u ORDER BY (u.money + u.stockMoney) DESC")
    List<User> findAllOrderByTotalAssetsDesc();

    boolean existsByLoginId(String loginId);
}
