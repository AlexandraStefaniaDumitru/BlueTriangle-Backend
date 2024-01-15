package bug.squashers.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityWithoutDateDTO {

    private ObjectId activityId;
    private String adultName;
    private String childName;
    private List<String> activityDates;
    private String duration;
    private String description;
}
