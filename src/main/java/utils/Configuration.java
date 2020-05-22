package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class Configuration {
    static private Configuration _instance = null;
    static private String databaseUser = null;
    static private String databasePassword = null;
    static private String databaseUrl = null;

    /**
     * Load database configuration from file
     */
    private Configuration(){
        try {
            InputStream configuration_file = new FileInputStream(new File("src/main/resources/configuration.properties"));
            Properties configuration = new Properties();
            configuration.load(configuration_file);
            _instance.databaseUser = configuration.getProperty("databaseUser");
            _instance.databasePassword = configuration.getProperty("databasePassword");
            _instance.databaseUrl = configuration.getProperty("databaseUrl");
        }
        catch(Exception e){
            System.out.println("Configuration file not found");
        }
    }

    public static Configuration getInstance(){
        if(_instance == null)
            _instance = new Configuration();
        return _instance;
    }

    public static String getDatabaseUser() {
        if(_instance == null)
            _instance = new Configuration();
        return databaseUser;
    }

    public static String getDatabasePassword() {
        if(_instance == null)
            _instance = new Configuration();
        return databasePassword;
    }

    public static String getDatabaseUrl() {
        if(_instance == null)
            _instance = new Configuration();
        return databaseUrl;
    }
}
