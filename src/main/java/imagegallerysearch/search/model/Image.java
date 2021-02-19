package imagegallerysearch.search.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Data
@Entity
public class Image {
    @Id
    private String id;
    private String author;
    private String camera;
    private String tags;
    private String croppedPicture;
    private String fullPicture;
}
