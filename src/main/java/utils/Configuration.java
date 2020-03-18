package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class Configuration {
    static private final Configuration _instance = new Configuration();
    static public String databaseUser;
    static public String databasePassword;
    static public String databaseUrl;

    public Configuration(){
        try {
            InputStream configuration_file = new FileInputStream(new File("src/main/resources/configuration.properties"));
            Properties configuration = new Properties();
            configuration.load(configuration_file);
            databaseUser = configuration.getProperty("databaseUser");
            databasePassword = configuration.getProperty("databasePassword");
            databaseUrl = configuration.getProperty("databaseUrl");
        }
        catch(Exception e){
            System.out.println("Configuration file not found");
        }
    }

    public static Configuration getInstance(){
        return _instance;
    }
}
