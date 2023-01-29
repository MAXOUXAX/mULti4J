package me.maxouxax.multi4j;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MultiConfig {

    private final String authUrl = "https://auth.univ-lorraine.fr";
    private final String dataUrl = "https://multi.univ-lorraine.fr";
    private String username = "";
    private String password = "";

    public MultiConfig() {
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Loads the config file from the given path
     *
     * @param path The path to the config file
     * @return The config file or null if it doesn't exist
     */
    public static MultiConfig loadConfig(String path){
        return loadConfig(new File(path));
    }

    /**
     * Loads the config file from the given path
     *
     * @param configFile The File object of the config file
     * @return The config file or null if it doesn't exist
     */
    public static MultiConfig loadConfig(File configFile) {
        if (configFile.exists()) {
            Yaml yaml = new Yaml(new Constructor(MultiConfig.class));
            yaml.setBeanAccess(BeanAccess.FIELD);
            try {
                InputStream inputStream = new FileInputStream(configFile);
                return yaml.loadAs(inputStream, MultiConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
