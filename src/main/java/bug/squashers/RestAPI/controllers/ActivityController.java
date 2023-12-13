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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*" )
@RequestMapping("/api/activities")
public class ActivityController {

    private final static Logger log= LogManager.getLogger(ActivityController.class);
    @Autowired
    private Service service;

    /*
    ORIGINAL:
    public ResponseEntity<List<Activity>> getActivities()
     */
    @GetMapping
    public ResponseEntity<List<MultipleChildActivity>> getActivities() {
        log.info("ActivityController - getActivities");
        /* ORIGINAL
        return new ResponseEntity<List<Activity>>(service.findAll(), HttpStatus.OK);
        */
        return new ResponseEntity<List<MultipleChildActivity>>(service.findAll(), HttpStatus.OK);
    }

    /*
    ORIGINAL:
    public List<Activity> getActivitiesForUserById(@PathVariable String userId)
     */
    @GetMapping("/id/{userId}")
    public List<MultipleChildActivity> getActivitiesForUserById(@PathVariable String userId) {
        log.info("ActivityController - getActivitiesForUserById : {}",userId);
        return service.getActivitiesForUserById(new ObjectId(userId));
    }

    /*
    ORIGINAL:
    public List<Activity> getActivitiesForUserByUsernmae(@PathVariable String username)
     */
    @GetMapping("/{username}")
    public List<MultipleChildActivity> getActivitiesForUserByUsernmae(@PathVariable String username) {
        log.info("ActivityController - getActivitiesForUserByUsernmae : {}",username);
        return service.getActivitiesForUserByUsername(username);
    }

    /*
    ORIGINAL:
    public ResponseEntity<?> bookAppointment(@RequestBody DTO dto)
     */
    @PostMapping()
    public ResponseEntity<?> bookAppointment(@RequestBody MultipleChildDTO dto)
    {
        log.info("ActivityController - bookAppointment : {}",dto);
        /*
        ORIGINAL:
        System.out.println(dto.getChildName());
        Child child= service.findChild(dto.getChildName()).orElse(null);
        */
        List<Child> children = service.findChilrenByNames(dto.getChildrenNames());
        User user = service.findUser(dto.getAdultName()).orElse(null);
        /*
        ORIGINAL:
        Activity activity = Activity.builder()
                .child(child)
                .adult(user)
                .date(dto.getActivityDate())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .build();
        Activity savedActivity = this.service.saveActivity(activity);
        this.service.saveActivity(savedActivity);
        */
        MultipleChildActivity multipleChildActivity = MultipleChildActivity.builder()
                .children(children)
                .adult(user)
                .date(dto.getActivityDate())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .build();
        MultipleChildActivity savedActivity = this.service.saveActivity(multipleChildActivity);
        this.service.saveActivity(savedActivity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
