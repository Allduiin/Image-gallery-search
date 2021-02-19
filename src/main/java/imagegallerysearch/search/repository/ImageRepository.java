package imagegallerysearch.search.repository;

import imagegallerysearch.search.model.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, String> {
    @Query("SELECT im from Image im WHERE im.id LIKE %:searchTerm% "
            + "OR im.author  LIKE %:searchTerm% "
            + "OR im.camera  LIKE %:searchTerm% "
            + "OR im.tags  LIKE %:searchTerm% "
            + "OR im.croppedPicture  LIKE %:searchTerm% "
            + "OR im.fullPicture  LIKE %:searchTerm% ")
    List<Image> search(@Param("searchTerm") String searchTerm);
}
