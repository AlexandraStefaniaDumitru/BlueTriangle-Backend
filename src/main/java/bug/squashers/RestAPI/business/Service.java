package bug.squashers.RestAPI.business;

import bug.squashers.RestAPI.infrastructure.ActivityRepository;
import bug.squashers.RestAPI.infrastructure.ChildRepository;
import bug.squashers.RestAPI.infrastructure.MultipleChildActivityRepository;
import bug.squashers.RestAPI.infrastructure.UserRepository;
import bug.squashers.RestAPI.model.*;
import bug.squashers.RestAPI.utils.Utils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    private final static Logger log= LogManager.getLogger(Service.class);
    @Autowired
    private UserRepository userRepository;

    /* ORIGINAL
    @Autowired
    private ActivityRepository activityRepository;
     */

    @Autowired
    private MultipleChildActivityRepository multipleChildActivityRepository;


    @Autowired
    private ChildRepository childRepository;
    public List<User> findAllUsers() {
        System.out.println("findAllUsers");
        log.info("Service - findAllUsers");
        return userRepository.findAll();
    }
    public User findUserByUsername(String username) {
        log.info("Service - findByUsername : {}",username);
        return userRepository.findByUsername(username).orElse(null);
    }

    /*
     ORIGINAL:
     public List<Activity> getActivitiesForUserById(ObjectId userId)
     */
    public List<MultipleChildActivity> getActivitiesForUserById(ObjectId userId) {
        log.info("Service - getActivitiesForUserById : {}",userId);
        /* ORIGINAL
        return activityRepository.findByChild_IdOrAdult_Id(userId, userId);
         */
        return multipleChildActivityRepository.findMultipleChildActivitiesByAdult_Id(userId);
    }

    /*
    ORIGINAL:
    public List<Activity> getActivitiesForUserByUsername(String username)
     */
    public List<MultipleChildActivity> getActivitiesForUserByUsername(String username) {
        log.info("Service - getActivitiesForUserByUsername : {}",username);
        Optional<User> user = userRepository.findByUsername(username);
        ObjectId userId = user.get().getId();
        /*
        ORIGINAL:
        return activityRepository.findByChild_IdOrAdult_Id(userId, userId);
         */
        return multipleChildActivityRepository.findMultipleChildActivitiesByAdult_Id(userId);
    }

    /*
    ORIGINAL:
    public List<Activity> findAll()
     */
    public List<MultipleChildActivity> findAll() {
        log.info("Service - findAll");
        /*
        ORIGINAL:
        return activityRepository.findAll();
         */
        return multipleChildActivityRepository.findAll();
    }

    /*
    ORIGINAL:
    public Activity saveActivity(Activity activity)
     */
    public MultipleChildActivity saveActivity(MultipleChildActivity activity) {
        log.info("Service - saveActivity : {}", activity);
        /*
        ORIGINAL:
        return activityRepository.save(activity);
         */
        return multipleChildActivityRepository.save(activity);
    }

    public User createUser(String username,String description, String password,String date) {
        log.info("Service - createUser : {},{},{},{}",username,description,password,date);
        String formattedDate = Utils.getFormattedDate(date);
        return userRepository.insert(new User(username,password,description,formattedDate));
    }

    public List<Child> findAllChildren() {
        log.info("Service - findAllChildren");
        return childRepository.findAll();
    }

    public Optional<Child> findChild(ObjectId childID) { return childRepository.findById(childID);
    }

    public Optional<User> findUser(ObjectId userID) {
        return userRepository.findById(userID);
    }

    public Optional<Child> findChild(String childName) {
        log.info("Service - findChild : {}",childName);
        return childRepository.findByName(childName);
    }

    public Optional<User> findUser(String userName) {
        log.info("Service - findUser : {}",userName);
        return userRepository.findByUsername(userName);
    }

    public Optional<Child> findChildByName(String name) {
        log.info("Service - findChildByName : {}",name);
        return childRepository.findByName(name);
    }

    public Optional<User> login(String username, String password) {
        log.info("Service - login : {}, {}",username,password);
        Optional<User> user = userRepository.findUserByUsernameAndPassword(username,password);
        return user;
    }

    public List<Child> findChilrenByNames(List<String> childrenNames) {
        List<Child> children = new ArrayList<>();
        for (String name :
                childrenNames) {
            children.add(childRepository.findByName(name).get());
        }
        return children;
    }
}