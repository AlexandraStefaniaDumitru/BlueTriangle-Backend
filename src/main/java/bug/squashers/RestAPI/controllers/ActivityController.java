package bug.squashers.RestAPI.controllers;

import bug.squashers.RestAPI.business.Service;
import bug.squashers.RestAPI.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/activities")
public class ActivityController {

    private final static Logger log = LogManager.getLogger(ActivityController.class);
    @Autowired
    private Service service;

    @GetMapping
    public ResponseEntity<List<Activity>> getActivities() {
        log.info("ActivityController - getActivities");
        return new ResponseEntity<List<Activity>>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/id/{userId}")
    public List<Activity> getActivitiesForUserById(@PathVariable String userId) {
        log.info("ActivityController - getActivitiesForUserById : {}", userId);
        return service.getActivitiesForUserById(new ObjectId(userId));
    }

    @GetMapping("/{username}")
    public List<Activity> getActivitiesForUserByUsernmae(@PathVariable String username) {
        log.info("ActivityController - getActivitiesForUserByUsernmae : {}", username);
        return service.getActivitiesForUserByUsername(username);
    }

    @PostMapping()
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
                .build();
        Activity savedActivity = this.service.saveActivity(activity);
        this.service.saveActivity(savedActivity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addMultipleDateForAppointment(@RequestBody ActivityWithoutDateDTO activityWithoutDateDTO) {
        Child child = service.findChild(activityWithoutDateDTO.getChildName()).orElse(null);
        User user = service.findUser(activityWithoutDateDTO.getAdultName()).orElse(null);
        for (int i = 0; i < activityWithoutDateDTO.getActivityDates().size(); i++) {
            Activity activity = Activity.builder()
                    .child(child)
                    .adult(user)
                    .date(activityWithoutDateDTO.getActivityDates().get(i))
                    .description(activityWithoutDateDTO.getDescription())
                    .duration(activityWithoutDateDTO.getDuration())
                    .build();
            this.service.saveActivity(activity);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
