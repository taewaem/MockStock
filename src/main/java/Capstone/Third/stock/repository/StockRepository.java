package Capstone.Third.stock.repository;

import Capstone.Third.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {

    @Query("select s from Stock s where s.stockName = :stockName")
    public Stock findStockName(@Param("stockName") String stockName);

}
