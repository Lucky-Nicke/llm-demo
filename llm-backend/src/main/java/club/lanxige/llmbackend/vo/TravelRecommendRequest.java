package club.lanxige.llmbackend.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelRecommendRequest {

    @NotNull(message = "城市不能为空")
    private String city;

    @NotNull(message = "预算不能为空")
    @Min(value = 100, message = "预算不能小于100元")
    private Integer budget;

    @NotNull(message = "天数不能为空")
    @Min(value = 1, message = "天数不能小于1天")
    @Max(value = 30, message = "天数不能大于30天")
    private Integer days;
}
