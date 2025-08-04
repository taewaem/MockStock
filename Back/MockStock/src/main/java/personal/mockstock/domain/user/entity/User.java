package personal.mockstock.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(
                name = "loginId_unique",
                columnNames = {"loginId"}
        )})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String password;
    private String userName;
    private Long money;
    private Long stockMoney;    // 평가 금액

    @Builder
    public User(String loginId, String password, String userName, Long money) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.money = money;
    }

    public void updateMoney(Long money) {
        this.money = money;
    }
    public void updateStockMoney(Long stockMoney) {
        this.stockMoney = stockMoney;
    }

}