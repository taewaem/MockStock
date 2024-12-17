package Capstone.Third;

import Capstone.Third.api.kis.response.KisVolumeRankRes;
import Capstone.Third.api.kis.service.KisVolumeRankService;
import Capstone.Third.stock.entity.Stock;
import Capstone.Third.stock.repository.StockRepository;
import Capstone.Third.user.entity.SessionConst;
import Capstone.Third.user.entity.User;
import Capstone.Third.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Slf4j
public class HomeController {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    Map<Integer, KisVolumeRankRes.Output> map = new HashMap<>();

    @GetMapping("/")
    private String home(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {


        log.info("home controller");

        List<User> users = userRepository.findAll();

        List<User> userList = users.stream()
                .sorted((u1, u2) -> Long.compare(u2.getMoney() + u2.getStockMoney(), u1.getMoney() + u1.getStockMoney())) // 유저돈 기준 내림차순 정렬
                .limit(5)
                .toList();
        model.addAttribute("userList", userList);

        List<Stock> allStock = stockRepository.findAll();
        Collections.shuffle(allStock); // 리스트를 무작위로 섞음
        List<Stock> top10Stocks = allStock.stream().limit(10).toList(); //10개만 선택
        model.addAttribute("stocks", top10Stocks);


        if (loginUser == null) {
            log.info("main home");
            return "home";
        }

        log.info("login home");
        model.addAttribute("user", loginUser);

        return "users/loginHome";
    }
}
