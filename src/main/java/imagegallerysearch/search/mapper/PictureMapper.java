package imagegallerysearch.search.mapper;

import imagegallerysearch.search.model.Image;
import imagegallerysearch.search.model.ImageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PictureMapper {
    public Image ImageResponseDtoToImage(ImageResponseDto imageResponseDto) {
        Image image = new Image();
        image.setId(imageResponseDto.getId());
        image.setAuthor(imageResponseDto.getAuthor());
        image.setCamera(imageResponseDto.getCamera());
        image.setTags(imageResponseDto.getTags());
        image.setCroppedPicture(imageResponseDto.getCropped_picture());
        image.setFullPicture(imageResponseDto.getFull_picture());
        return image;
    }
}
