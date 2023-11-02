package bug.squashers.RestAPI.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTO {

    private String adultName;
    private String childName;

    private String activityDate;
    private String duration;

    private String description;


}
