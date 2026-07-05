package club.lanxige.llmbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyItinerary {

    private Integer day;
    private String date;
    private TimeSlot morning;
    private TimeSlot afternoon;
    private TimeSlot evening;
}
