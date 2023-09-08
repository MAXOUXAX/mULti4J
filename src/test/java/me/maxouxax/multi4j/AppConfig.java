package me.maxouxax.multi4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties properties = new Properties();

    static {
        try {
            // Check if running on GitHub Actions (using a GitHub-specific environment variable)
            String githubToken = System.getenv("GITHUB_TOKEN");
            if (githubToken != null) {
                // Load properties from command-line arguments
                properties.setProperty("username", System.getProperty("username"));
                properties.setProperty("password", System.getProperty("password"));
            } else {
                // Load properties from the config.properties file for local development
                try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                    properties.load(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUsername() {
        return properties.getProperty("username");
    }

    public static String getPassword() {
        return properties.getProperty("password");
    }

}
