package Capstone.Third.userStock.entity;

import Capstone.Third.stock.entity.Stock;
import Capstone.Third.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_stock")
public class UserStock {

    @Id
    @Column(name = "user_stock_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;


    private Integer averagePrice;                //손익 분기
    private Integer profitAndLoss;               //평가 손익
    private double profitAndLossRate;           //평간 손익 퍼센트
    private Integer count;                       //총 구매 수
    private Integer currentTotalPrice;           //평가 금액
    private Integer buyTotalPrice;               //매수 원금
    private LocalDateTime localDateTime;

}


