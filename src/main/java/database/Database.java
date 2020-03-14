package database;

import org.flywaydb.core.Flyway;

public class Database {
    Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost/knihkupectvodb", "postgres", "admin").load();

    public void updateDatabase() {
        flyway.migrate();
    }
}

