package bug.squashers.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityActivityDTO {
    private List<String> children;
    private List<String> adults;
    private String organizer;
    private String date;
    private String duration;
    private String description;
}
