package personal.mockstock.domain.interest.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.mockstock.domain.stock.entity.Stock;
import personal.mockstock.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "interest",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_stock_interest",
                        columnNames = {"user_id", "stock_id"}
                )
        })
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Interest(User user, Stock stock) {
        this.user = user;
        this.stock = stock;
        this.createdAt = LocalDateTime.now();
    }
}