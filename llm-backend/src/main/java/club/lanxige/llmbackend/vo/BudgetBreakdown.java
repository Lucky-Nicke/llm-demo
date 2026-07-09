package club.lanxige.llmbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetBreakdown {

    private Object accommodation;
    private Object food;
    private Object transportation;
    private Object tickets;
    private Object other;
}
