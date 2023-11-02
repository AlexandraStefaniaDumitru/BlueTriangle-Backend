package bug.squashers.RestAPI.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "*" )
@RequestMapping("/api/photos")
public class PhotoController {

    private final static Logger log= LogManager.getLogger(PhotoController.class);

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        log.info("PhotoController - getPhoto : {}",filename);
        try {
            // Load the photo file from the "photos" directory
            Path photoPath = Paths.get("src/main/resources/photos", filename);
            Resource resource = new UrlResource(photoPath.toUri());

            if (resource.exists()) {
                // Return the photo file as a response
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // Set the appropriate content type for your photos
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}