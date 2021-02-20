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
    private final static String API_KEY = "23567b218376f79d9415";

    public String authorize() throws IOException {
        URL url = new URL("http://interview.agileengine.com/auth");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        String reqBody = "{\"apiKey\":\"" + API_KEY + "\"}";
        writer.write(reqBody);
        writer.flush();
        writer.close();
        connection.connect();
        if (connection.getResponseCode() != 200) {
            throw new AuthorizationException("Incorrect Http status response");
        }
        Token token = new Token(connection.getInputStream());
        if (!token.getAuth()) {
            throw new AuthorizationException("false authorization bad Api key");
        }
        return token.getToken();
    }

    @Data
    private class Token {
        private Boolean auth;
        private String token;

        private Token(InputStream inputStream) throws IOException {
            String input;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                input = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            input = input.substring(1, input.length() - 1).replaceAll("\"","");
            String[] params = input.split(",");
            auth = Boolean.parseBoolean(params[0].split(":")[1]);
            token = params[1].split(":")[1];
        }
    }
}