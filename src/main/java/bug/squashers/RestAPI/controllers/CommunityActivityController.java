package bug.squashers.RestAPI.controllers;

import bug.squashers.RestAPI.business.Service;
import bug.squashers.RestAPI.manager.EmailSenderManager;
import bug.squashers.RestAPI.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/community-activities")
public class CommunityActivityController {
    private final static Logger log = LogManager.getLogger(ActivityController.class);
    @Autowired
    private Service service;
    @Autowired
    private EmailSenderManager emailSenderManager;

    @GetMapping("/all-community-activities")
    public ResponseEntity<List<CommunityActivity>> getCommunityActivities() {
        log.info("CommunityActivityController - getCommunityActivities");
        return new ResponseEntity<>(service.findAllCommunityActivities(), HttpStatus.OK);
    }

    @GetMapping("/all-community-activities/{username}")
    public ResponseEntity<List<CommunityActivity>> getCommunityActivitiesForUsername(@PathVariable String username) {
        log.info("CommunityActivityController - getCommunityActivities");
        User organizer = service.findUser(username).orElse(null);
        return new ResponseEntity<>(service.findCommunityActivityByOrganizer(organizer), HttpStatus.OK);
    }

    @PostMapping("/join-activity/{username}")
    public ResponseEntity<?> joinCommunityActivity(@RequestBody List<String> dto, @PathVariable String username) {
        String organizerName = dto.get(0);
        String date = dto.get(1);
        User organizer = service.findUser(organizerName).orElse(null);
        User joinee = service.findUser(username).orElse(null);
        CommunityActivity communityActivity = new CommunityActivity();
        List<CommunityActivity> communityActivities = service.findCommunityActivityByOrganizer(organizer);
        for (CommunityActivity ca : communityActivities) {
            if (ca.getDate().equals(date)) {
                List<User> adults = ca.getAdults();
                adults.add(joinee);
                ca.setAdults(adults);
                communityActivity = ca;
            }
        }
        service.saveCommunityActivity(communityActivity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createCommunityActivity(@RequestBody CommunityActivityDTO dto) {
        log.info("CommunityActivityController - createCommunityActivity : {}", dto);
        List<String> childrenList = dto.getChildren();
        List<Child> childList = new ArrayList<>();
        User organizer = service.findUser(dto.getOrganizer()).orElse(null);
        for (String childName : childrenList) {
            Child child = service.findChild(childName).orElse(null);
            childList.add(child);
        }
        List<String> adultsList = dto.getAdults();
        List<User> adults = new ArrayList<>();
        for (String adultName : adultsList) {
            User adult = service.findUser(adultName).orElse(null);
            adults.add(adult);
        }
        List<String> dates = List.of(dto.getDate().split(", "));
        for (String date : dates) {
            CommunityActivity communityActivity = CommunityActivity.builder()
                    .children(childList)
                    .adults(adults)
                    .organizer(organizer)
                    .date(date)
                    .description(dto.getDescription())
                    .duration(dto.getDuration())
                    .isVerified(false)
                    .hasFeedback(false)
                    .build();
            CommunityActivity savedCommunityActivity = this.service.saveCommunityActivity(communityActivity);
            this.service.saveCommunityActivity(savedCommunityActivity);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verify")
    @Description("Verifies an activity")
    public ResponseEntity<Activity> verifyActivity(@RequestBody Map<String, String> payload) {
        log.info("CommunityActivityController - verifyActivity : {} {}", payload.get("description"), payload.get("date"));
        CommunityActivity activity = service.findCommunityActivityByDescriptionAndDate(payload.get("description"), payload.get("date"));
        activity.setVerified(true);
        log.info(activity);
        service.saveCommunityActivity(activity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verify-false")
    @Description("Verifies an activity")
    public ResponseEntity<Activity> verifyActivityFalse(@RequestBody Map<String, String> payload) {
        log.info("CommunityActivityController - verifyActivity : {} {}", payload.get("description"), payload.get("date"));
        CommunityActivity activity = service.findCommunityActivityByDescriptionAndDate(payload.get("description"), payload.get("date"));
        log.info(activity);
        service.deleteCommunityActivity(activity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/feedback")
    @Description("Updates score based on feedback for the activity's organizer")
    public ResponseEntity<Activity> feedbackActivity(@RequestBody ActivityFeedbackDTO activityFeedback) {
        log.info("CommunityActivityController - feedback : {} {} {}", activityFeedback.getDate(), activityFeedback.getDescription(), activityFeedback.getFeedback());
        CommunityActivity activity = service.findCommunityActivityByDescriptionAndDate(activityFeedback.getDescription(), activityFeedback.getDate());
        activity.setHasFeedback(true);
        log.info(activity);
        service.saveCommunityActivity(activity);
        service.feedbackCommunityActivity(activity, activityFeedback.getFeedback());
        User organizer = activity.getOrganizer();
        int oldScore = organizer.getScore();
        int newScore = activityFeedback.getFeedback();
        if (newScore < 0) {
            organizer.setEmail("lrngrigorescu@gmail.com");
            String to = organizer.getEmail();
            String subject = "We're sorry!";
            int finalScore = newScore + oldScore;
            String body = "After a discussion with our children, the feedback was not a very good one.\n"
                    + "Don't worry!This is beneficial not only for us but also for you.\n" +
                    "Don't stop finding the best version of you!\n" +
                    "The score obtained at this activity is " + newScore + " and you current score is " + finalScore
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
                    + "The score obtained at this activity is " + newScore + " and you current score is " + finalScore
                    + "\n" + "Sincerely, Blue Triangle Team!";
            emailSenderManager.sendEmail(to, subject, body);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
