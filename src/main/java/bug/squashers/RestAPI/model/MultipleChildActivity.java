package bug.squashers.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.*;

@Document(collection = "multiple_child_activities")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultipleChildActivity {
    @Id
    private ObjectId id;

    @DocumentReference
    private List<Child> children;

    @DocumentReference
    private User adult;

    private String date;

    private String duration;

    private String description;
}
