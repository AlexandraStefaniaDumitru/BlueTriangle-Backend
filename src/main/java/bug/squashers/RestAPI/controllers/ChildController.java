package bug.squashers.RestAPI.controllers;

import bug.squashers.RestAPI.business.Service;
import bug.squashers.RestAPI.model.Child;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*" )
@RequestMapping("/api/children")
public class ChildController {

    private final static Logger log= LogManager.getLogger(ChildController.class);
    private final Service service;
    public ChildController(Service service) {
        this.service = service;
    }

    @GetMapping
    @Description("Retrieves all the children")
    public ResponseEntity<List<Child>> getChildren() {
        log.info("ChildController - getChildren");
        return new ResponseEntity<List<Child>>(service.findAllChildren(), HttpStatus.OK);
    }

    @GetMapping("/known/{username}")
    @Description("Retrieves all the children that interacted with a specific user")
    public ResponseEntity<List<Child>> getKnownChildren(@PathVariable String username) {
        log.info("ChildController - getChildren");
        return new ResponseEntity<List<Child>>(service.findAllKnownChildren(username), HttpStatus.OK);
    }
    @GetMapping("/{name}")
    @Description("Retrieves a child based on his username")
    public ResponseEntity<Optional<Child>> getChildByUsername(@PathVariable String name){
        log.info("ChildController - getChildByUsername : {}",name);
        return new ResponseEntity<Optional<Child>>(service.findChildByName(name), HttpStatus.OK);
    }

}