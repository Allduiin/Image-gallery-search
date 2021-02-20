package imagegallerysearch.search.service;

import java.io.IOException;

public interface AuthorisationService {
    String authorize(String apiKey) throws IOException;
}
