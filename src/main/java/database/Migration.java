package database;

import org.flywaydb.core.Flyway;
import utils.Configuration;

public class Migration {
    public static void main(String[] args){
        Configuration configuration = Configuration.getInstance();
        Flyway flyway = Flyway.configure().dataSource(Configuration.databaseUrl,
                Configuration.databaseUser, Configuration.databasePassword).load();
        flyway.migrate();
        System.out.println("Database migrated to newest version.");
    }
}

