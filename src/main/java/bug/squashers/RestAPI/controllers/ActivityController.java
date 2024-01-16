package bug.squashers.RestAPI.controllers;

import bug.squashers.RestAPI.business.Service;
import bug.squashers.RestAPI.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/activities")
public class ActivityController {

    private final static Logger log = LogManager.getLogger(ActivityController.class);
    @Autowired
    private Service service;

    @GetMapping
    @Description("Retrieves all the activities from DB")
    public ResponseEntity<List<Activity>> getActivities() {
        log.info("ActivityController - getActivities");
        return new ResponseEntity<List<Activity>>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id/{userId}")
    @Description("Retrieves all the activities for a particular user by his id")
    public List<Activity> getActivitiesForUserById(@PathVariable String userId) {
        log.info("ActivityController - getActivitiesForUserById : {}", userId);
        return service.getActivitiesForUserById(new ObjectId(userId));
    }

    @GetMapping("/{username}")
    @Description("Retrieves all the activities for a particular user by his username")
    public List<Activity> getActivitiesForUserByUsernmae(@PathVariable String username) {
        log.info("ActivityController - getActivitiesForUserByUsernmae : {}", username);
        return service.getActivitiesForUserByUsername(username);
    }

    @PostMapping()
    @Description("Creates an appointment for an activity with a child")
    public ResponseEntity<?> bookAppointment(@RequestBody DTO dto) {
        log.info("ActivityController - bookAppointment : {}", dto);
        System.out.println(dto.getChildName());
        Child child = service.findChild(dto.getChildName()).orElse(null);
        User user = service.findUser(dto.getAdultName()).orElse(null);
        Activity activity = Activity.builder()
                .child(child)
                .adult(user)
                .date(dto.getActivityDate())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .isVerified(false)
                .hasFeedback(false)
                .build();
        Activity savedActivity = this.service.saveActivity(activity);
        this.service.saveActivity(savedActivity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/multiple-dates")
    @Description("Adds multiple dates for an appointment made by a user")
    public ResponseEntity<?> addMultipleDateForAppointment(@RequestBody ActivityWithoutDateDTO activityWithoutDateDTO) {
        log.info("ActivityController - bookAppointment : {}", activityWithoutDateDTO);
        Child child = service.findChild(activityWithoutDateDTO.getChildName()).orElse(null);
        User user = service.findUser(activityWithoutDateDTO.getAdultName()).orElse(null);
        for (int i = 0; i < activityWithoutDateDTO.getActivityDates().size(); i++) {
            Activity activity = Activity.builder()
                    .child(child)
                    .adult(user)
                    .date(activityWithoutDateDTO.getActivityDates().get(i))
                    .description(activityWithoutDateDTO.getDescription())
                    .duration(activityWithoutDateDTO.getDuration())
                    .isVerified(false)
                    .hasFeedback(false)
                    .build();
            this.service.saveActivity(activity);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verify")
    @Description("Verifies an activity")
    public ResponseEntity<Activity> verifyActivity(@RequestBody Map<String, String> payload) {
        log.info("ActivityController - verifyActivity : {} {}", payload.get("description"), payload.get("date"));
        Activity activity = service.findActivityByDescriptionAndDate(payload.get("description"), payload.get("date"));
        activity.setVerified(true);
        log.info(activity);
        service.saveActivity(activity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/feedback")
    @Description("Updates score based on feedback for the activity's organizer")
    public ResponseEntity<Activity> feedbackActivity(@RequestBody ActivityFeedbackDTO activityFeedback) {
        log.info("ActivityController - feedback : {} {} {}", activityFeedback.getDate(), activityFeedback.getDescription(), activityFeedback.getFeedback());
        Activity activity = service.findActivityByDescriptionAndDate(activityFeedback.getDescription(), activityFeedback.getDate());
        activity.setHasFeedback(true);
        log.info(activity);
        service.saveActivity(activity);
        service.feedbackActivity(activity, activityFeedback.getFeedback());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
