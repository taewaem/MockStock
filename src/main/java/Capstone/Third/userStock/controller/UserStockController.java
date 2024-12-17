package Capstone.Third.userStock.controller;

import Capstone.Third.stock.service.StockService;
import Capstone.Third.user.entity.SessionConst;
import Capstone.Third.user.entity.User;
import Capstone.Third.user.repository.UserRepository;
import Capstone.Third.userStock.entity.UserStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserStockController {

    private final UserRepository userRepository;
    private final StockService stockService;

    @GetMapping("/user/{userId}")
    public String userInfo(@PathVariable("userId") Long userId, Model model) {

        Optional<User> userOptional = userRepository.findOne(userId);
        List<User> users = userRepository.findAll();

        //유저 랭킹 조회
        List<User> userList = users.stream()
                .sorted((u1, u2) -> Long.compare(u2.getMoney() + u2.getStockMoney(), u1.getMoney() + u1.getStockMoney())) // 유저돈 기준 내림차순 정렬
                .limit(5)
                .toList();
        model.addAttribute("userList", userList);


        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<UserStock> userStocks = user.getUserStocks();
            model.addAttribute("user", user);
            model.addAttribute("userStocks", userStocks);
        }

        return "users/userInfo";
    }

    @PostMapping("/userStock/bankrupt")
    public String bankrupt(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {

        log.info("bankrupt");
        // 특정 사용자의 주식을 삭제하는 서비스 호출
        Optional<User> userOptional = userRepository.findByLoginId(loginUser.getLoginId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            stockService.deleteAllStock(user.getLoginId());
            log.info("user name= {}", user.getUserName());
        }

        return "redirect:/";
    }

}
