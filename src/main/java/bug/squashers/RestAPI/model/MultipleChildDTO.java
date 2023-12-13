package bug.squashers.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChildDTO {
    private String adultName;
    private List<String> childrenNames;

    private String activityDate;
    private String duration;

    private String description;
}
