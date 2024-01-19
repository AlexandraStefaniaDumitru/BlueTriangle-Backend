package bug.squashers.RestAPI.controllers;

import bug.squashers.RestAPI.business.Service;
import bug.squashers.RestAPI.manager.EmailSenderManager;
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
    @Autowired
    private EmailSenderManager emailSenderManager;

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
        if (activity.isVerified()) {
            String subject = "Congratulations!Your activity is approved!";
            String body = """
                    One of our admins approved your activity.Let's go and put some smiles on children's faces!
                    Have fun!
                    Sincerely, Blue Triangle Team.""";
            activity.getAdult().setEmail("lrngrigorescu@gmail.com");
            emailSenderManager.sendEmail(activity.getAdult().getEmail(), subject, body);
        }
        log.info(activity);
        service.saveActivity(activity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
        @PostMapping("/verify-false")
        @Description("Verifies an activity")
        public ResponseEntity<Activity> verifyActivityFalse (@RequestBody Map < String, String > payload){
            log.info("ActivityController - verifyActivity : {} {}", payload.get("description"), payload.get("date"));
            Activity activity = service.findActivityByDescriptionAndDate(payload.get("description"), payload.get("date"));
            log.info(activity);
            service.deleteActivity(activity);
            String subject = "We're sorry!";
            String body = "The activity you proposed has not been approved.Try again, don't loose your hope!";
            emailSenderManager.sendEmail(activity.getAdult().getEmail(), subject, body);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        @PostMapping("/feedback")
        @Description("Updates score based on feedback for the activity's organizer")
        public ResponseEntity<Activity> feedbackActivity (@RequestBody ActivityFeedbackDTO activityFeedback){
            log.info("ActivityController - feedback : {} {} {}", activityFeedback.getDate(), activityFeedback.getDescription(), activityFeedback.getFeedback());
            Activity activity = service.findActivityByDescriptionAndDate(activityFeedback.getDescription(), activityFeedback.getDate());
            activity.setHasFeedback(true);
            log.info(activity);
            service.saveActivity(activity);
            service.feedbackActivity(activity, activityFeedback.getFeedback());
            User organizer = activity.getAdult();
            int oldScore = organizer.getScore();
            int newScore = activityFeedback.getFeedback();
            if (newScore < 0) {
                organizer.setEmail("lrngrigorescu@gmail.com");
                String to = organizer.getEmail();
                String subject = "We're sorry!";
                int finalScore = newScore + oldScore;
                String body = "After a discussion with our children, the feedback was not a very good one.\n"
                        + "Don't worry! This is beneficial not only for us but also for you.\n" +
                        "Don't stop finding the best version of you!\n" +
                        "The score obtained at this activity is " + newScore + " and your current score is " + finalScore
                        + "\n" +
                        "Sincerely, Blue Triangle Team!";
                emailSenderManager.sendEmail(to, subject, body);
            } else {
                organizer.setEmail("lrngrigorescu@gmail.com");
                String to = organizer.getEmail();
                String subject = "We have good news for you!";
                int finalScore = newScore + oldScore;
                String body = "After a discussion with our children, the feedback was a really good one.\n"
                        + "Keep up doing great job! Continue to find the best version of you anywhere.\n"
                        + "The score obtained at this activity is " + newScore + " and your current score is " + finalScore
                        + "\n" + "Sincerely, Blue Triangle Team!";
                emailSenderManager.sendEmail(to, subject, body);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
