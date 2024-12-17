package Capstone.Third.stock.service;


import Capstone.Third.stock.entity.Stock;
import Capstone.Third.stock.repository.StockRepository;
import Capstone.Third.user.entity.User;
import Capstone.Third.user.repository.UserRepository;
import Capstone.Third.userStock.entity.UserStock;
import Capstone.Third.userStock.repository.UserStockRepository;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository stockRepository;
    private final UserStockRepository userStockRepository;
    private final UserRepository userRepository;

    /**
     * 주식 매수
     */
    @Transactional
    public void buyStock(String loginId, String stockId, Integer count) {

        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        Optional<Stock> stockOptional = stockRepository.findById(stockId);

        if (userOptional.isPresent() && stockOptional.isPresent()) {
            User user = userOptional.get();
            Stock stock = stockOptional.get();
            if (user.getMoney() >= (long) stock.getStockPrice() * count) {
                UserStock userStock = userStockRepository.findByLoginIdAndStockId(user.getLoginId(), stock.getStockId());
                if (userStock == null) {
                    userStock = new UserStock();
                    userStock.setUser(user);
                    userStock.setStock(stock);
                    userStock.setCount(count);
                    userStock.setAveragePrice(stock.getStockPrice());
                    userStock.setProfitAndLoss(stock.getStockPrice() - userStock.getAveragePrice());
                    userStock.setProfitAndLossRate(((double) (stock.getStockPrice() - userStock.getAveragePrice()) / userStock.getAveragePrice()) * 100);
                    userStock.setLocalDateTime(LocalDateTime.now());
                    userStock.setBuyTotalPrice(userStock.getAveragePrice() * userStock.getCount());               //내가 구매한 평균 단가
                    userStock.setCurrentTotalPrice(stock.getStockPrice() * userStock.getCount());                 //현재 가
                } else {
                    int newCount = userStock.getCount() + count;

                    userStock.setAveragePrice((userStock.getAveragePrice() * userStock.getCount() + stock.getStockPrice() * count) / newCount);
                    userStock.setCount((newCount));
                    userStock.setBuyTotalPrice(userStock.getAveragePrice() * newCount);                             //내가 구매한 평균 단가
                    userStock.setCurrentTotalPrice(stock.getStockPrice() *  newCount);                               //현재 가
                    userStock.setProfitAndLoss((stock.getStockPrice() - userStock.getAveragePrice()) * newCount);
                    userStock.setProfitAndLossRate(((double) (stock.getStockPrice() - userStock.getAveragePrice()) / userStock.getAveragePrice()) * 100);
                    userStock.setLocalDateTime(LocalDateTime.now());
                }

                user.setMoney(user.getMoney() - ((long) stock.getStockPrice() * count));

                userStockRepository.save(userStock);
                userRepository.save(user);

                getUserStockBuyTotalPrice(loginId);
            } else{
                log.error("insufficient balance");
                throw new RuntimeException("insufficient balance"); // Optional exception throwing
            }

        } else{
            log.error("don't find user or stock");
            throw new RuntimeException("don't find user or stock"); // Optional exception throwing
        }
    }

    /**
     * 주식 매도
     */
    @Transactional
    public void sellStock(String loginId, String stockId, Integer count) {
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        if (userOptional.isPresent() && stockOptional.isPresent()) {
            User user = userOptional.get();
            Stock stock = stockOptional.get();

            UserStock userStock = userStockRepository.findByLoginIdAndStockId(user.getLoginId(), stock.getStockId());

            if (userStock == null) {
                log.error("No stock Held");
                return;
            }

            if (userStock.getCount() < count) {
                log.error("more than stock held");
                return;
            } else {
                user.setMoney(user.getMoney() + ((long) stock.getStockPrice() * count));
                if (userStock.getCount().equals(count)) {
                    userStockRepository.delete(userStock);
                } else {
                    userStock.setBuyTotalPrice(userStock.getBuyTotalPrice() * (userStock.getCount() - count) / userStock.getCount());
                    userStock.setCount(userStock.getCount() - count);
                    userStock.setProfitAndLoss((stock.getStockPrice() - userStock.getAveragePrice()) * userStock.getCount());
                    userStock.setProfitAndLossRate(((double) (stock.getStockPrice() - userStock.getAveragePrice()) / userStock.getAveragePrice()) * 100);
                    userStock.setCurrentTotalPrice(userStock.getCurrentTotalPrice() - stock.getStockPrice() * count);
                    userStock.setLocalDateTime(LocalDateTime.now());


                    userStockRepository.save(userStock);
                }
                userRepository.save(user);

                getUserStockBuyTotalPrice(loginId);
            }
        }
    }

    /**
     * 주식 파산
     */
    @Transactional
    public void deleteAllStock(String loginId) {
        List<UserStock> userStockList = userStockRepository.findByLoginId(loginId);

        for (UserStock userStock : userStockList) {
            userStockRepository.delete(userStock);
        }
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setMoney(10000000L);       //초기 자금 상태로 변경
            user.setStockMoney(0L);         //평가 손익 0
            userRepository.save(user);
        }
    }

    public void getUserStockBuyTotalPrice(String loginId) {
        List<UserStock> byLoginId = userStockRepository.findByLoginId(loginId);
        Optional<User> userOptional = userRepository.findByLoginId(loginId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long money = 0L;
            for (UserStock userStock : byLoginId){
                money += userStock.getCurrentTotalPrice();
            }
            user.setStockMoney(money);

            userRepository.save(user);
        }
    }


    @Transactional
    public void updateStockPrice() {

        List<UserStock> userStockAll = userStockRepository.findAll();

        if (userStockAll.isEmpty()) {
            log.info("No user stocks found. Skipping price update.");
            return;
        }
        else {
            for (UserStock userStock : userStockAll) {
                userStock.setCurrentTotalPrice(userStock.getStock().getStockPrice() *  userStock.getCount());                               //현재 가
                userStock.setProfitAndLoss((userStock.getStock().getStockPrice() - userStock.getAveragePrice()) * userStock.getCount());
                userStock.setProfitAndLossRate(((double) (userStock.getStock().getStockPrice() - userStock.getAveragePrice()) / userStock.getAveragePrice()) * 100);

                userStockRepository.save(userStock);
                userRepository.save(userStock.getUser());

                getUserStockBuyTotalPrice(userStock.getUser().getLoginId());
            }
        }
    }

}
