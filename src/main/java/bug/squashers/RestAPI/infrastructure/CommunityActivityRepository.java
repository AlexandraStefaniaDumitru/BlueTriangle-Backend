package bug.squashers.RestAPI.infrastructure;

import bug.squashers.RestAPI.model.Activity;
import bug.squashers.RestAPI.model.CommunityActivity;
import bug.squashers.RestAPI.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityActivityRepository extends MongoRepository<CommunityActivity, ObjectId> {
    List<CommunityActivity> findByOrganizer(User organizer);
    CommunityActivity findByDescriptionAndDate(String description, String date);
}
