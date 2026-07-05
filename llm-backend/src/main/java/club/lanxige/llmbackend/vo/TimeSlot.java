package club.lanxige.llmbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {

    private String spot;
    private String duration;
    private String ticket;
    private String transportation;
    private String description;
}
