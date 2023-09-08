package me.maxouxax.multi4j;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.maxouxax.multi4j.exceptions.MultiLoginException;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class MultiClient {

    private final HttpClient httpClient;
    private MultiConfig multiConfig;
    private String currentToken;
    private String refreshToken;

    private MultiClient() {
        this.httpClient = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
    }

    /**
     * Connects to the mULti web application with the given config, storing the tokens if the login was successful.
     *
     * @throws MultiLoginException If the login failed
     */
    public void connect() throws MultiLoginException {
        Gson gson = new Gson();

        String authObject = "username=" + multiConfig.getUsername() + "&" +
                "password=" + URLEncoder.encode(multiConfig.getPassword(), StandardCharsets.UTF_8) + "&";

        String authTicket;
        try {
            authTicket = getTicket(multiConfig.getAuthUrl(), "/login?service=" + multiConfig.getDataUrl() + "/login", authObject);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new MultiLoginException(e);
        }

        if (authTicket == null) {
            throw new MultiLoginException("Unable to get auth ticket, check your credentials");
        }

        JsonObject jsonObject = gson.fromJson("{\"operationName\":\"casAuth\",\"variables\":{\"token\":" + authTicket + "},\"query\":\"query casAuth($token: String!) {\\n  casAuth(token: $token)\\n}\\n\"}", JsonObject.class);
        JsonObject authToken;
        try {
            authToken = makeGQLRequest(jsonObject);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new MultiLoginException(e);
        }

        JsonArray tokenArray = authToken.get("data").getAsJsonObject().get("casAuth").getAsJsonArray();
        this.currentToken = tokenArray.get(0).getAsString();
        this.refreshToken = tokenArray.get(1).getAsString();
    }

    /**
     * Make a GQL request to the mULti GQL API.
     * If the client is connected, the request will be made with the current token and the refresh token.
     *
     * @param query the query to send
     * @return the response of the query
     * @throws IOException          If an I/O error occurs
     * @throws URISyntaxException   If the URI is invalid
     * @throws InterruptedException If the thread is interrupted
     */
    public JsonObject makeGQLRequest(JsonObject query) throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(multiConfig.getDataUrl() + "/graphql");

        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json");

        if (currentToken != null && refreshToken != null) {
            builder = builder
                    .header("x-refresh-token", refreshToken)
                    .header("x-token", currentToken);
        }

        HttpRequest gqlRequest = builder.POST(HttpRequest.BodyPublishers.ofString(query.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(gqlRequest, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        return gson.fromJson(response.body(), JsonObject.class);
    }

    /**
     * Get the ticket from the CAS server allowing to log in to the mULti application
     * May return null if the login failed
     *
     * @param baseUrl     The base url of the CAS server
     * @param endpoint    The endpoint of the CAS server
     * @param credentials The credentials to send to the CAS server
     * @return The ticket if the login was successful, null otherwise
     * @throws URISyntaxException   If the URI is invalid
     * @throws IOException          If an I/O error occurs
     * @throws InterruptedException If the thread is interrupted
     */
    private String getTicket(String baseUrl, String endpoint, String credentials) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(baseUrl + endpoint);
        HttpRequest loginPageRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();

        HttpResponse<String> loginPageResponse = httpClient.send(loginPageRequest, HttpResponse.BodyHandlers.ofString());
        String executionToken = loginPageResponse.body().split("<input type=\"hidden\" name=\"execution\" value=\"")[1].split("\"/>")[0];

        String formData = credentials + "&execution=" + executionToken + "&_eventId=submit";

        HttpRequest authRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = httpClient.send(authRequest, HttpResponse.BodyHandlers.ofString());

        Optional<String> location = response.headers().firstValue("Location");
        return location.map(s -> s.split("ticket=")[1]).orElse(null);
    }

    public MultiConfig getMultiConfig() {
        return multiConfig;
    }

    public void setMultiConfig(MultiConfig multiConfig) {
        this.multiConfig = multiConfig;
    }

    public boolean isConnected() {
        return currentToken != null && refreshToken != null;
    }

    public static class Builder {

        private final MultiClient multiClient;

        public Builder() {
            this.multiClient = new MultiClient();
        }

        public Builder withMultiConfig(MultiConfig multiConfig) {
            this.multiClient.setMultiConfig(multiConfig);
            return this;
        }

        public MultiClient build() {
            return this.multiClient;
        }

    }

}
