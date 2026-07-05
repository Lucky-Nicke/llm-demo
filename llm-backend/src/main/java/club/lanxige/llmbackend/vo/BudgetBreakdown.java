package club.lanxige.llmbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetBreakdown {

    private Integer accommodation;
    private Integer food;
    private Integer transportation;
    private Integer tickets;
    private Integer other;
}
