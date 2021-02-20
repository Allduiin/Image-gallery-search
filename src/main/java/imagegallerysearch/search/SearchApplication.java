package imagegallerysearch.search;

import imagegallerysearch.search.service.AuthorisationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SearchApplication.class, args);
        run.getBean(AuthorisationService.class).authorize("23567b218376f79d9415");
    }

}
