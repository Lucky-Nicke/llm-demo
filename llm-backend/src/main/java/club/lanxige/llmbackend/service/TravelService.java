package club.lanxige.llmbackend.service;

import club.lanxige.llmbackend.vo.TravelRecommendRequest;
import club.lanxige.llmbackend.vo.TravelRecommendVO;
import org.springframework.beans.factory.annotation.Value;

public interface TravelService {
    TravelRecommendVO recommend(TravelRecommendRequest request);
}
