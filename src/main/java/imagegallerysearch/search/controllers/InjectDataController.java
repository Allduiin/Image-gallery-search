package imagegallerysearch.search.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import imagegallerysearch.search.exceptions.AuthorizationException;
import imagegallerysearch.search.mapper.PictureMapper;
import imagegallerysearch.search.model.ImageResponseDto;
import imagegallerysearch.search.authorization.AuthorizationService;
import imagegallerysearch.search.service.ImageService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor

public class InjectDataController {
    private static final String API_KEY = "23567b218376f79d9415";
    private final ImageService imageService;
    private final AuthorizationService authorizationService;
    private final PictureMapper pictureMapper;

    @Scheduled(fixedRate = 3600000L)
    public void injectDataToDb() {
        String token = authorizationService.authorize(API_KEY);
        try {
            URL url = new URL("http://interview.agileengine.com/images");
            String jsonString = createConnectionAndTakeJsonString(token, url);
            Pictures pictures;
            try {
                pictures = new ObjectMapper().readValue(jsonString, Pictures.class);
            } catch (JsonProcessingException e) {
                throw new AuthorizationException("JSON parsing problem", e);
            }
            addAllPictures(pictures, token);
            while (pictures.getHasMore()) {
                url = new URL("http://interview.agileengine.com/images?page=" + (pictures.getPage() + 1L));
                jsonString = createConnectionAndTakeJsonString(token,url);
                try {
                    pictures = new ObjectMapper().readValue(jsonString, Pictures.class);
                } catch (JsonProcessingException e) {
                    throw new AuthorizationException("JSON parsing problem", e);
                }
                addAllPictures(pictures, token);
            }
        } catch (IOException e) {
            throw new RuntimeException("Getting images connection problem", e);
        }
    }

    private Boolean addAllPictures(Pictures pictures, String token) {
        Arrays.stream(pictures.getPictures())
                .forEach(p -> imageService
                        .save(pictureMapper
                                .imageResponseDtoToImage(getFullPictureById(p.getId(), token))));
        return true;
    }

    private ImageResponseDto getFullPictureById(String id, String token) {
        ImageResponseDto imageResponseDto;
        try {
            URL url = new URL("http://interview.agileengine.com/images/" + id);
            String jsonString = createConnectionAndTakeJsonString(token, url);
            try {
                imageResponseDto = new ObjectMapper().readValue(jsonString, ImageResponseDto.class);
            } catch (JsonProcessingException e) {
                throw new AuthorizationException("JSON parsing problem", e);
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Problem with taking all params of picture with id:" + id, e);
        }
        return imageResponseDto;
    }

    private String createConnectionAndTakeJsonString(String token, URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.connect();
        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Incorrect Http status response");
        }
        String jsonString;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            jsonString = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        connection.disconnect();
        return jsonString;
    }

    @Data
    private static class Pictures {
        private CroppedImage[] pictures;
        private Long page;
        private Long pageCount;
        private Boolean hasMore;

        @Data
        private static class CroppedImage {
            private String id;
            private String cropped_picture;
        }
    }
}
