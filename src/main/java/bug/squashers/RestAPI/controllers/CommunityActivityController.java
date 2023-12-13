package bug.squashers.RestAPI.controllers;

import bug.squashers.RestAPI.business.Service;
import bug.squashers.RestAPI.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/community-activities")
public class CommunityActivityController {
    private final static Logger log = LogManager.getLogger(ActivityController.class);
    @Autowired
    private Service service;

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
        for(CommunityActivity ca : communityActivities){
            if(ca.getDate().equals(date)){
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
        for(String adultName: adultsList){
            User adult = service.findUser(adultName).orElse(null);
            adults.add(adult);
        }
        CommunityActivity communityActivity = CommunityActivity.builder()
                .children(childList)
                .adults(adults)
                .organizer(organizer)
                .date(dto.getDate())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .build();
        CommunityActivity savedCommunityActivity = this.service.saveCommunityActivity(communityActivity);
        this.service.saveCommunityActivity(savedCommunityActivity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
