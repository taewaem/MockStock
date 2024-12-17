package Capstone.Third.stock.controller;

import Capstone.Third.api.kis.service.KisCurrentPriceService;
import Capstone.Third.api.kis.service.StockInfoService;
import Capstone.Third.stock.entity.Stock;
import Capstone.Third.stock.repository.StockRepository;
import Capstone.Third.stock.service.StockService;
import Capstone.Third.user.entity.SessionConst;
import Capstone.Third.user.entity.User;
import Capstone.Third.user.repository.UserRepository;
import Capstone.Third.userStock.entity.UserStock;
import Capstone.Third.userStock.repository.UserStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@RestController //RestController를 사용하면 @ResponseBody가 내장되어 있다. 이것은 객체가 JSON으로 반한됨
@Slf4j
@Controller
@RequiredArgsConstructor
public class StockController {

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final UserStockRepository userStockRepository;
    private final KisCurrentPriceService currentPriceService;
    private final StockInfoService stockInfoService;
    private final StockService stockService;

    @GetMapping("/all_stock")
    public String allStock(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        List<Stock> allStock = stockRepository.findAll();
        model.addAttribute("stocks", allStock);
        model.addAttribute("user", loginUser);

        if (loginUser == null) {
            log.info("user null");
            return "/stock/allStock";
        }

        log.info("user: {}", loginUser.getUserName());
        model.addAttribute("user", loginUser);

        return "/stock/allStock";
    }

    @PostMapping("/stockInfo/{stockId}")
    public String stockGetInfo(@PathVariable("stockId") String stockId, Model model) {

        log.info("postMapping stockId: {}", stockId);


        currentPriceService.getPrice(stockId);


        return "redirect:/stockInfo/" + stockId;
    }

    @GetMapping("/stockInfo/{stockId}")
    public String stockInfo(@PathVariable("stockId") String stockId, Model model)
    {

        Optional<Stock> stockOptional = stockRepository.findById(stockId);

        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            model.addAttribute("stock", stock);
            stockInfoService.getStockInfo(stockId);
        }


        log.info("getMapping stockId: {}", stockId);
        return "/stock/stockInfo";
    }


    //주식 용어
    @GetMapping("/stockTerm")
    public String stockTerm() {
        return "/stock/stockTerm";
    }


    @PostMapping("/buyStock/{stockId}")
    public String postBuyStock(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                           @PathVariable("stockId") String stockId,
                           @RequestParam("count") Integer count,
                               Model model)
    {
        if(loginUser == null){
            return "redirect:";
        }

        Optional<Stock> stock = stockRepository.findById(stockId);
        model.addAttribute("stock", stock);
        log.info("Post buy stockId: {}", stockId);
        stockService.buyStock(loginUser.getLoginId(), stockId, count);

        return  "redirect:/stockInfo/" + stockId;
    }

    @GetMapping("/buyStock/{stockId}")
    public String buyStock(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                           @PathVariable("stockId") String stockId,
                           Model model)
    {
        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        Optional<User> userOptional = userRepository.findByLoginId(loginUser.getLoginId());
        if (stockOptional.isPresent() && userOptional.isPresent()) {
            Stock stock = stockOptional.get();
            Long userMoney = userOptional.get().getMoney() / stock.getStockPrice();
            model.addAttribute("stock", stock);
            model.addAttribute("userMoney", userMoney);
            log.info("Get buy stockId: {}", stockId);
        }
        return "stock/buyStock";
    }

    @PostMapping("/sellStock/{stockId}")
    public String postSellStock(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                            @PathVariable("stockId") String stockId,
                            @RequestParam("count") Integer count,
                                Model model)
    {
        if(loginUser == null){
            return "redirect:";
        }

        Optional<Stock> stock = stockRepository.findById(stockId);
        model.addAttribute("stock", stock);
        log.info("Post sell stockId: {}", stockId);
        stockService.sellStock(loginUser.getLoginId(), stockId, count);
        return  "redirect:/stockInfo/" + stockId;
    }

    @GetMapping("/sellStock/{stockId}")
    public String sellStock(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                            @PathVariable("stockId") String stockId,
                            Model model)
    {

        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            Integer maxCount = 0;
            List<UserStock> userStockList = userStockRepository.findByLoginId(loginUser.getLoginId());
            if (userStockList == null) {
                model.addAttribute("maxCount", maxCount);
            }
            for (UserStock userStock : userStockList) {
                if(stock.getStockId().equals(userStock.getStock().getStockId())) {
                    maxCount = userStock.getCount();
                    model.addAttribute("maxCount", maxCount);
                    break;
                }
            }
            model.addAttribute("stock", stock);
            log.info("Get sell stockId: {}", stockId);
        }

        return "stock/sellStock";
    }
}
