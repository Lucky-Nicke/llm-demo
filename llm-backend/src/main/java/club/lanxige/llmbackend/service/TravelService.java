package club.lanxige.llmbackend.service;

import club.lanxige.llmbackend.vo.TravelRecommendRequest;
import club.lanxige.llmbackend.vo.TravelRecommendVO;

public interface TravelService {

    TravelRecommendVO recommend(TravelRecommendRequest request);
}
