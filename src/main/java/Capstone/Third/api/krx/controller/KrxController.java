package Capstone.Third.api.krx.controller;

import Capstone.Third.api.krx.response.KrxPriceResponse;
import Capstone.Third.api.krx.response.KrxResponse;
import Capstone.Third.api.krx.util.KrxUtil;
import Capstone.Third.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class KrxController {

    private final KrxUtil krxUtil;

//    @GetMapping("/all_stock")
//    public KrxPriceResponse allStock(){
//        return krxUtil.getStockPrice();
//    }

}
