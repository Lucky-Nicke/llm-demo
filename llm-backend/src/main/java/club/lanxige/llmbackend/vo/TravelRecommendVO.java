package club.lanxige.llmbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelRecommendVO {

    private Boolean success;
    private String city;
    private Integer days;
    private Integer totalBudget;
    private List<DailyItinerary> dailyItinerary;
    private List<String> tips;
    private List<String> warnings;
    private BudgetBreakdown budgetBreakdown;
    private String error;
    private String rawResponse;
}
