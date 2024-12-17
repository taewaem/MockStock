package Capstone.Third.interest.entity;


import Capstone.Third.stock.entity.Stock;
import Capstone.Third.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interest")
public class Interest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "is_interested")
    @Setter
    private boolean isInterested;

    @Builder
    public Interest(User user,Stock stock,boolean isInterested) {
        this.user = user;
        this.stock = stock;
        this.isInterested = isInterested;
    }

}

