package database;

import org.hibernate.Session;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void getSessionFactory() throws SQLException, ClassNotFoundException {
        Session session = Database.getInstance().getSessionFactory().openSession();
        assertNotEquals("There are no data in table books", 1, 0);
        session.close();
    }
}