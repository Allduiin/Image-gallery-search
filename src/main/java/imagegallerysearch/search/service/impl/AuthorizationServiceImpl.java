package imagegallerysearch.search.service.impl;

import imagegallerysearch.search.exceptions.AuthorizationException;
import imagegallerysearch.search.service.AuthorisationService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorisationService {
    Token token;

    public String authorize(String apiKey) {
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
            token = new Token(connection.getInputStream());
            if (!token.getAuth()) {
                throw new AuthorizationException("false authorization bad Api key");
            }
        } catch (IOException e) {
            throw new AuthorizationException("Problem at connection", e);
        }
        return token.getToken();
    }

    @Data
    private static class Token {
        private Boolean auth;
        private String token;

        private Token(InputStream inputStream)  {
            String input;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                input = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                throw new AuthorizationException("Problem at getting response from connection", e);
            }
            input = input.substring(1, input.length() - 1).replaceAll("\"","");
            String[] params = input.split(",");
            auth = Boolean.parseBoolean(params[0].split(":")[1]);
            token = params[1].split(":")[1];
        }
    }
}
