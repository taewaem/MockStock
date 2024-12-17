package Capstone.Third.user.entity;


import Capstone.Third.userStock.entity.UserStock;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
//유니크 로그인 id 생성
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
    private String userName;    //시용자 이름

    @Setter
    private Long money;         //보유 현금
    @Setter
    private Long stockMoney;        //평가 금액


    @OneToMany(mappedBy = "user")
    private List<UserStock> userStocks;

    @Builder
    public User(String loginId, String password, String userName, Long money) {
        this.loginId = loginId;
        this.password = password;
        this.userName = userName;
        this.money = money;
    }

}
