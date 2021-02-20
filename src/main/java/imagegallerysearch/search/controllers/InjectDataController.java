package imagegallerysearch.search.controllers;

import imagegallerysearch.search.service.AuthorisationService;
import imagegallerysearch.search.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class InjectDataController {
    private final ImageService imageService;
    private final AuthorisationService authorisationService;
    private static final String API_KEY = "23567b218376f79d9415";

    private boolean InjectDataToDb() {
        String token = authorisationService.authorize(API_KEY);
        return false;
    }
}
