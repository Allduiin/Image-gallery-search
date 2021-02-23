package imagegallerysearch.search.service.impl;

import imagegallerysearch.search.model.Image;
import imagegallerysearch.search.repository.ImageRepository;
import imagegallerysearch.search.service.ImageService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private ImageRepository imageRepository;

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public List<Image> search(String searchTerm) {
        return imageRepository.search(searchTerm);
    }
}
