package imagegallerysearch.search.controllers;

import imagegallerysearch.search.model.Image;
import imagegallerysearch.search.service.ImageService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/search")
    public List<Image> search(@RequestParam String searchTemplate) {
        return imageService.search(searchTemplate);
    }
}
