package Capstone.Third.interest.service;

import Capstone.Third.interest.entity.Interest;
import Capstone.Third.interest.repository.InterestRepository;
import Capstone.Third.stock.entity.Stock;
import Capstone.Third.stock.repository.StockRepository;
import Capstone.Third.user.entity.User;
import Capstone.Third.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    //관심 목록 추가
    public void plusInterested(String loginId,String stockId){

        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        Optional<Stock> stockOptional = stockRepository.findById(stockId);

        if (userOptional.isPresent() && stockOptional.isPresent()) {
            User user = userOptional.get();
            Stock stock = stockOptional.get();
            Interest interest = interestRepository.findByLoginIdAndStockId(user.getLoginId(), stock.getStockId());
            if(interest == null){
                interest = Interest.builder()
                        .user(user)
                        .stock(stock)
                        .isInterested(true)
                        .build();
                interestRepository.save(interest);
                log.info("{}: {} 관심 목록 추가 완료", user.getLoginId(), stock.getStockName());
            }
        }
    }

    public void deleteInterested(String loginId, String stockId){

        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        Optional<Stock> stockOptional = stockRepository.findById(stockId);

        if (userOptional.isPresent() && stockOptional.isPresent()) {
            User user = userOptional.get();
            Stock stock = stockOptional.get();
            Interest interest = interestRepository.findByLoginIdAndStockId(user.getLoginId(), stock.getStockId());
            if(interest != null){
                interest.setInterested(false);
                interestRepository.delete(interest);
                log.info("관심 목록 삭제 완료: {}", stock.getStockName());
            }
        }
    }
}
