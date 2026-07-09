package club.lanxige.llmbackend.service.impl;

import club.lanxige.llmbackend.service.TravelService;
import club.lanxige.llmbackend.utils.LLMUtils;
import club.lanxige.llmbackend.vo.TravelRecommendRequest;
import club.lanxige.llmbackend.vo.TravelRecommendVO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class TravelServiceImpl implements TravelService {
    @Value("${llm.api-key}")
    private String apiKey;
    @Value("${llm.base-url}")
    private String apiUrl;
    @Value("${llm.model}")
    private String model;
    private ObjectMapper objectMapper;

    private LLMUtils llmUtils;

    @PostConstruct
    public void init() {
        this.llmUtils = new LLMUtils(apiKey, apiUrl, model);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public TravelRecommendVO recommend(TravelRecommendRequest request) {
        TravelRecommendVO result = new TravelRecommendVO();
        Integer budget = request.getBudget();
        Integer days = request.getDays();
        String city = request.getCity();
        String prompt = buildTravelPrompt(city, budget, days);

        try {
            String rsp = llmUtils.callLLM(null, prompt);
            return parseTravelResponse(rsp);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setError("旅游推荐失败" + e.getMessage());
            return result;
        }
    }

    // service旅游推荐返回数据处理
    private TravelRecommendVO parseTravelResponse(String response) {
        TravelRecommendVO result = new TravelRecommendVO();

        try {
            String jsonContent = extractJson(response);
            if (jsonContent != null) {
                result = objectMapper.readValue(jsonContent, TravelRecommendVO.class);
            } else {
                result.setSuccess(false);
                result.setError("未能从响应中提取JSON");
                result.setRawResponse(response);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setError("JSON解析失败");
            result.setRawResponse(response);
        }

        return result;
    }

    private String extractJson(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }

        String[] patterns = {
                "```json\n([\\s\\S]*?)\n```",
                "```\n([\\s\\S]*?)\n```"
        };

        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(response);
            if (m.find()) {
                return m.group(1);
            }
        }

        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }

        return null;
    }

    private String buildTravelPrompt(String city, Integer budget, Integer days) {
        return "你是一个专业的旅游规划师，擅长根据用户的需求生成详细的旅行行程。\n\n" +
                "请根据以下信息为用户生成一份详细的旅游规划：\n" +
                "- 目的地城市：" + city + "\n" +
                "- 预算：" + budget + "元\n" +
                "- 旅行天数：" + days + "天\n\n" +
                "要求：\n" +
                "1. 每天的行程安排（上午、下午、晚上）\n" +
                "2. 每个景点的详细介绍\n" +
                "3. 交通建议\n" +
                "4. 预算分配明细\n" +
                "5. 注意事项\n\n" +
                "请以JSON格式输出，结构如下：\n" +
                "{\n" +
                "  \"success\": true,\n" +
                "  \"city\": \"城市名\",\n" +
                "  \"days\": 天数,\n" +
                "  \"totalBudget\": 总预算,\n" +
                "  \"dailyItinerary\": [\n" +
                "    {\n" +
                "      \"day\": 1,\n" +
                "      \"date\": \"第1天\",\n" +
                "      \"morning\": {\n" +
                "        \"spot\": \"景点名称\",\n" +
                "        \"duration\": \"游览时长\",\n" +
                "        \"ticket\": \"门票价格\",\n" +
                "        \"transportation\": \"交通方式\",\n" +
                "        \"description\": \"景点介绍\"\n" +
                "      },\n" +
                "      \"afternoon\": {\n" +
                "        \"spot\": \"景点名称\",\n" +
                "        \"duration\": \"游览时长\",\n" +
                "        \"ticket\": \"门票价格\",\n" +
                "        \"transportation\": \"交通方式\",\n" +
                "        \"description\": \"景点介绍\"\n" +
                "      },\n" +
                "      \"evening\": {\n" +
                "        \"spot\": \"活动名称\",\n" +
                "        \"duration\": \"活动时长\",\n" +
                "        \"ticket\": \"费用\",\n" +
                "        \"transportation\": \"交通方式\",\n" +
                "        \"description\": \"活动介绍\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"budgetBreakdown\": {\n" +
                "    \"accommodation\": 住宿费用,\n" +
                "    \"food\": 餐饮费用,\n" +
                "    \"transportation\": 交通费用,\n" +
                "    \"tickets\": 门票费用,\n" +
                "    \"other\": 其他费用\n" +
                "  },\n" +
                "  \"tips\": [\"提示1\", \"提示2\", \"提示3\"],\n" +
                "  \"warnings\": [\"注意事项1\", \"注意事项2\"]\n" +
                "}\n\n" +
                "请确保JSON格式正确，可以被解析。";
    }
}
