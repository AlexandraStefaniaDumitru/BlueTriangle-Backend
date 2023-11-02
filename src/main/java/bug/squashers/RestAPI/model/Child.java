package bug.squashers.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "children")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Child {
    @Id
    private ObjectId id;
    private String name;
    private List<String> favoriteActivities;
    private String description;
    private String birthdate;
    private String photoURI;
    private double lat;
    private double lng;


}