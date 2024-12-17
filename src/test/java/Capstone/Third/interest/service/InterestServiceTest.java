package Capstone.Third.interest.service;

import Capstone.Third.interest.entity.Interest;
import Capstone.Third.interest.repository.InterestRepository;
import Capstone.Third.stock.entity.Stock;
import Capstone.Third.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class InterestServiceTest {

    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private InterestService interestService;

    @Test
    void 테스트() {

        //given
        User user = User.builder()
                .loginId("kim")
                .money(100000L)
                .password("kim")
                .userName("kim")
                .build();
        Stock stock = Stock.builder()
                .rate("100%")
                .stockId("10000")
                .changes("-100")
                .stockPrice(10000)
                .stockName("삼전")
                .build();

        //when
//        Long interestId = interestService.plusInterested(user, stock);

        List<Interest> all = interestRepository.findAll();
        System.out.println(all);
//        List<Interest> stockId = interestRepository.findByStockId("10000");
        //then

    }

}