package Capstone.Third.stock.service;

import Capstone.Third.stock.entity.Stock;
import Capstone.Third.stock.repository.StockRepository;
import Capstone.Third.user.entity.User;
import Capstone.Third.user.repository.UserRepository;
import Capstone.Third.userStock.entity.UserStock;
import Capstone.Third.userStock.repository.UserStockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserStockRepository userStockRepository;

    @AfterEach
    void setUp() {
        // 테스트 유저와 주식 객체 설정
        User user = User.builder()
                .loginId("user")
                .password("user")
                .money(100000L)
                .build();

        Stock stock = Stock.builder()
                .stockId("A12345")
                .stockPrice(5000)
                .build();
    }

    @Test
    void buyStock_success() {
        // 유저와 주식이 존재하는 경우
        Optional<User> userOptional = userRepository.findByLoginId("user");
        Optional<Stock> stockOptional = stockRepository.findById("A12345");


        if (userOptional.isPresent() && stockOptional.isPresent()) {
            User user = userOptional.get();
            Stock stock = stockOptional.get();

            stockService.buyStock(user.getLoginId(), stock.getStockId(), 10);

            assertEquals(50000, user.getMoney());  // 5000 * 10 = 50000 차감됨
        }
    }

//    @Test
//    void buyStock_insufficientFunds() {
//        // 유저가 금액 부족한 경우
//        user.setMoney(1000L);  // 잔액을 적게 설정
//
//        when(userRepository.findByLoginId("testUser")).thenReturn(Optional.of(testUser));
//        when(stockRepository.findById("A12345")).thenReturn(Optional.of(testStock));
//
//        // 주식 매수 로직 실행
//        stockService.buyStock("testUser", "A12345", 10);
//
//        // 금액 부족으로 인해 저장 메서드가 호출되지 않아야 함
//        verify(userStockRepository, never()).save(any(UserStock.class));
//        verify(userRepository, never()).save(testUser);
//
//        // 유저 금액이 변경되지 않았는지 확인
//        assertEquals(1000, testUser.getMoney());
//    }
//
//    @Test
//    void sellStock_success() {
//        // 유저가 주식을 보유한 경우
//        UserStock userStock = new UserStock();
//        userStock.setCount(10);
//        userStock.setStock(testStock);
//        userStock.setUser(testUser);
//
//        when(userRepository.findByLoginId("testUser")).thenReturn(Optional.of(testUser));
//        when(stockRepository.findById("A12345")).thenReturn(Optional.of(testStock));
//        when(userStockRepository.findByUserIdAndStockId(testUser.getLoginId(), testStock.getStockId())).thenReturn(userStock);
//
//        // 주식 매도 로직 실행
//        stockService.sellStock("testUser", "A12345", 5);
//
//        // 유저 금액이 추가되었는지 확인
//        assertEquals(125000, testUser.getMoney());  // 100000 + 5000 * 5 = 125000
//
//        // UserStock 수량이 줄었는지 확인
//        assertEquals(5, userStock.getCount());
//
//        // UserStock 저장 호출 여부 확인
//        verify(userStockRepository, times(1)).save(userStock);
//        verify(userRepository, times(1)).save(testUser);
//    }
//
//    @Test
//    void sellStock_insufficientStock() {
//        // 유저가 주식을 충분히 보유하지 않은 경우
//        UserStock userStock = new UserStock();
//        userStock.setCount(3);
//        userStock.setStock(testStock);
//        userStock.setUser(testUser);
//
//        when(userRepository.findByLoginId("testUser")).thenReturn(Optional.of(testUser));
//        when(stockRepository.findById("A12345")).thenReturn(Optional.of(testStock));
//        when(userStockRepository.findByUserIdAndStockId(testUser.getLoginId(), testStock.getStockId())).thenReturn(userStock);
//
//        // 주식 매도 로직 실행
//        stockService.sellStock("testUser", "A12345", 5);
//
//        // UserStock 저장이 호출되지 않아야 함
//        verify(userStockRepository, never()).save(userStock);
//        verify(userRepository, never()).save(testUser);
//
//        // 유저 금액이 변경되지 않았는지 확인
//        assertEquals(100000, testUser.getMoney());
//    }
}
