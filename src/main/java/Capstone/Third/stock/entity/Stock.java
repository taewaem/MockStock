package Capstone.Third.stock.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stock")
public class Stock {


    @Id
    @Column(name = "stock_id")
    private String stockId;                 //주식 코드
    private String stockName;               //주식 이름
    private Integer stockPrice;             //가격
    private String changes;                 //전일 대비
    private String rate;                    //전일 대비율
    private String marketCode;               //시장 구분

    @Builder
    public Stock(String stockId, String stockName, Integer stockPrice, String changes, String rate, String marketCode){
        this.stockId = stockId;
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.changes = changes;
        this.rate = rate;
        this.marketCode = marketCode;
    }


}
