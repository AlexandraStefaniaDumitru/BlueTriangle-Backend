package bug.squashers.RestAPI.infrastructure;

import bug.squashers.RestAPI.model.Activity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends MongoRepository<Activity, ObjectId> {
    List<Activity> findByChild_IdOrAdult_Id(ObjectId childId, ObjectId adultId);
    Activity findByDescriptionAndDate(String description, String date);

}
