package imagegallerysearch.search.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import imagegallerysearch.search.exceptions.AuthorizationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    public String authorize(String apiKey) {
        String jsonString;
        try {
            URL url = new URL("http://interview.agileengine.com/auth");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            String reqBody = "{\"apiKey\":\"" + apiKey + "\"}";
            writer.write(reqBody);
            writer.flush();
            writer.close();
            connection.connect();
            if (connection.getResponseCode() != 200) {
                throw new AuthorizationException("Incorrect Http status response");
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                jsonString = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                throw new AuthorizationException("Problem at getting response from connection", e);
            }
        } catch (IOException e) {
            throw new AuthorizationException("Problem at connection", e);
        }
        Token token;
        try {
            token = new ObjectMapper().readValue(jsonString, Token.class);
        } catch (JsonProcessingException e) {
            throw new AuthorizationException("JSON parsing problem", e);
        }
        if (!token.getAuth()) {
            throw new AuthorizationException("false authorization bad Api key");
        }
        return token.getToken();
    }

    @Data
    private static class Token {
        private Boolean auth;
        private String token;
    }
}
