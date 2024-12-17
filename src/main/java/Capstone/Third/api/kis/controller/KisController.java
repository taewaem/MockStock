package Capstone.Third.api.kis.controller;


import Capstone.Third.api.kis.response.ClosingPriceRes;
import Capstone.Third.api.kis.response.KisVolumeRankRes;
import Capstone.Third.api.kis.response.CurrentPriceRes;
import Capstone.Third.api.kis.response.StockInfoRes;
import Capstone.Third.api.kis.service.KisClosingPriceService;
import Capstone.Third.api.kis.service.KisCurrentPriceService;
import Capstone.Third.api.kis.service.KisVolumeRankService;
import Capstone.Third.api.kis.service.StockInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
public class KisController {

    private final KisVolumeRankService kisVolumeRankService;
    private final KisCurrentPriceService kisPriceService;
    private final StockInfoService stockInfoService;
    private final KisClosingPriceService kisClosingPriceService;

    @GetMapping("/volume_rank")
    public Mono<KisVolumeRankRes> getVolumeRank() {
        return kisVolumeRankService.getVolumeRank();
    }

    @GetMapping("/curPriceApi")
    public Mono<CurrentPriceRes> getCurrentPrice(String stockId) {

        log.info("stockController stockId: {}", stockId);

        return kisPriceService.getPrice(stockId);
    }

    @GetMapping("stockInfoApi")
    public Mono<StockInfoRes> getStockinfo(@RequestParam("stockId") String stockId) {
        log.info("requeset param: {}", stockId);
        return stockInfoService.getStockInfo(stockId);
    }


}
