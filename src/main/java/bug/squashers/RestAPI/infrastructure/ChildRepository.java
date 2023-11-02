package bug.squashers.RestAPI.infrastructure;

import bug.squashers.RestAPI.model.Child;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends MongoRepository<Child, ObjectId> {
    @Override
    List<Child> findAll();

    Optional<Child> findByName(String childName);
}
