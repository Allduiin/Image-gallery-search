package imagegallerysearch.search.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Image {
    @Id
    String id;
    String link;
}
