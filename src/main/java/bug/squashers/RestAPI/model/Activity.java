package bug.squashers.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "activities")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Activity {
    @Id
    private ObjectId id;

    @DocumentReference
    private Child child;

    @DocumentReference
    private User adult;

    private String date;

    private String duration;

    private String description;

    private boolean isVerified;

    private boolean hasFeedback;
}
