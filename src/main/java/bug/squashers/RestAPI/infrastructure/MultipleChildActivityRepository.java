package bug.squashers.RestAPI.infrastructure;

import bug.squashers.RestAPI.model.MultipleChildActivity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MultipleChildActivityRepository extends MongoRepository<MultipleChildActivity, ObjectId> {
    List<MultipleChildActivity> findMultipleChildActivitiesByAdult_Id(ObjectId adultId);
}
