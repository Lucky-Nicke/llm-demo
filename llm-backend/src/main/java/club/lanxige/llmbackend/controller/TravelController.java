package club.lanxige.llmbackend.controller;

import club.lanxige.llmbackend.service.TravelService;
import club.lanxige.llmbackend.vo.Result;
import club.lanxige.llmbackend.vo.TravelRecommendRequest;
import club.lanxige.llmbackend.vo.TravelRecommendVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/travel")
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/recommend")
    public Result<TravelRecommendVO> recommend(@Valid @RequestBody TravelRecommendRequest request) {
        return Result.ok(travelService.recommend(request));
    }
}
