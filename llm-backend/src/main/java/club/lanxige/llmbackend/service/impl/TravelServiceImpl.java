package club.lanxige.llmbackend.service.impl;

import club.lanxige.llmbackend.service.TravelService;
import club.lanxige.llmbackend.vo.TravelRecommendRequest;
import club.lanxige.llmbackend.vo.TravelRecommendVO;
import org.springframework.stereotype.Service;

@Service
public class TravelServiceImpl implements TravelService {

    @Override
    public TravelRecommendVO recommend(TravelRecommendRequest request) {
        // TODO: 根据 city、budget、days 调用 LLM 生成旅行推荐
        return null;
    }
}
