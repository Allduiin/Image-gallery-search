package imagegallerysearch.search.service;


import imagegallerysearch.search.model.Image;
import imagegallerysearch.search.repository.ImageRepository;
import imagegallerysearch.search.service.impl.ImageServiceImpl;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ImageServiceTest {
    private static ImageService imageService;
    private static final String TEST_ID = "1jh2b123lj";
    private static final String SEARCH_TEMPLATE = "Falcon";


    @Test
    public void normalSaveTest() {
        Image example = new Image();
        Image expected = new Image();
        expected.setId(TEST_ID);
        ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
        imageService = new ImageServiceImpl(imageRepository);
        Mockito.when(imageRepository.save(example)).thenReturn(expected);
        Assert.assertEquals("Image service must correctly save and return image",
                expected, imageService.save(example));
    }

    @Test
    public void normalSearchTest() {
        Image expected1 = new Image();
        expected1.setTags(SEARCH_TEMPLATE);
        Image expected2 = new Image();
        expected2.setCamera(SEARCH_TEMPLATE);
        ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
        imageService = new ImageServiceImpl(imageRepository);
        Mockito.when(imageRepository.search(SEARCH_TEMPLATE))
                .thenReturn(List.of(expected1,expected2));
        Assert.assertEquals("Image service must correctly return searched images",
                List.of(expected1,expected2), imageService.search(SEARCH_TEMPLATE));
    }

    @Test
    public void emptySearchTest() {
        ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
        imageService = new ImageServiceImpl(imageRepository);
        Mockito.when(imageRepository.search(SEARCH_TEMPLATE))
                .thenReturn(Collections.emptyList());
        Assert.assertEquals("Image service must return empty list if it nothing found",
                Collections.emptyList(), imageService.search(SEARCH_TEMPLATE));
    }
}
