package imagegallerysearch.search.service;

import imagegallerysearch.search.model.Image;
import java.util.List;

public interface ImageService {
    Image save(Image image);

    List<Image> search(String searchTerm);
}
