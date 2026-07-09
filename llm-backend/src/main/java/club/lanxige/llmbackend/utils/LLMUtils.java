package club.lanxige.llmbackend.utils;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LLMUtils {
    @Value("${llm.api-key}")
    private String apiKey;
    @Value("${llm.base-url}")
    private String apiUrl;
    @Value("${llm.model}")
    private String model;
    OkHttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LLMUtils(String apiKey, String apiUrl, String model) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.model = model;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    // 调用大模型的接口

    public String callLLM(String sysProp, String userPrompt) {
        String requestBody = buildRequestBody(sysProp, userPrompt, model, false);

        // 创建一个网络请求
        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build();
        try (Response response = client.newCall(request).execute()) {
            String rsp = response.body().string();
            System.out.println("LLM响应状态: " + response.code());
            System.out.println("LLM响应体: " + rsp);
            if (!response.isSuccessful()) throw new IOException("LLM调用异常: " + response.code() + " body: " + rsp);
            return extractContent(rsp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 模型返回数据处理
    private String extractContent(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode choices = root.path("choices");
        if (choices.isArray() && choices.size() > 0) {
            return choices.get(0).path("message").path("content").asText();
        }
        return "";
    }

    public String buildRequestBody(String sysProp,
                                   String userPrompt,
                                   String model,
                                   Boolean stream) {
        return "{" +
                "\"model\":\"" + model + "\"," +
                "\"stream\":" + stream + "," +
                "\"messages\":[" +
                "{\"role\":\"system\",\"content\":\"" + sysProp + "\"}," +
                "{\"role\":\"user\",\"content\":\"" + escapeJson(userPrompt) + "\"}" +
                "]," +
                "\"temperature\":0.5" +
                "}";
    }


    // JSON字符串转义方法
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")      // 转义反斜杠
                .replace("\"", "\\\"")      // 转义双引号
                .replace("\n", "\\n")       // 转义换行符
                .replace("\r", "\\r")       // 转义回车符
                .replace("\t", "\\t");      // 转义制表符
    }
}
