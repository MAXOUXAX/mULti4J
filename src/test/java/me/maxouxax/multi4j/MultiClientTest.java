package me.maxouxax.multi4j;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.maxouxax.multi4j.crous.CrousRestaurant;
import me.maxouxax.multi4j.exceptions.MultiLoginException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class MultiClientTest {

    private MultiClient multiClient;

    @BeforeEach
    void setUp() throws URISyntaxException {
        String configPath = new URI(getClass().getClassLoader().getResource("config.properties").toString()).getPath();
        MultiConfig config = MultiConfig.loadConfig(configPath);
        multiClient = new MultiClient.Builder().withMultiConfig(config).build();
    }

    @AfterEach
    void tearDown() {
        multiClient = null;
    }

    @Test
    void setConfig() {
        MultiConfig config = new MultiConfig();
        config.setUsername("test");
        config.setPassword("test");
        multiClient.setMultiConfig(config);
        assertEquals("test", multiClient.getMultiConfig().getUsername());
        assertEquals("test", multiClient.getMultiConfig().getPassword());

        MultiConfig config2 = new MultiConfig();
        config2.setUsername("test2");
        config2.setPassword("test2");
        multiClient.setMultiConfig(config2);
        assertEquals("test2", multiClient.getMultiConfig().getUsername());
        assertEquals("test2", multiClient.getMultiConfig().getPassword());
    }

    @Test
    void connect() throws MultiLoginException {
        multiClient.connect();
        assertTrue(multiClient.isConnected());
    }

    @Test
    void makeGQLRequest() throws MultiLoginException, IOException, URISyntaxException, InterruptedException {
        multiClient.connect();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson("""
                {"operationName":"factuel","variables":{},"query":"query factuel {\\n  news {\\n    title\\n    image\\n    date\\n    description\\n    link\\n    __typename\\n  }\\n}\\n"}
                """, JsonObject.class);
        JsonObject response = multiClient.makeGQLRequest(jsonObject);
        assertTrue(response.has("data"));
    }

    @Test
    void crousRestaurants() throws MultiLoginException {
        multiClient.connect();
        assertDoesNotThrow(() -> {
            CrousRestaurant restaurantDetails = MultiHelper.getRestaurantDetails(multiClient, "(S)pace Brabois");
            assertEquals("(S)pace Brabois", restaurantDetails.getName());
            System.out.println(restaurantDetails);
            assertNotNull(restaurantDetails.getThumbnailUrl());
            assertNotNull(restaurantDetails.getImageUrl());
            assertNotNull(restaurantDetails.getDescription());
            assertNotNull(restaurantDetails.getMenus());
        });

    }

}