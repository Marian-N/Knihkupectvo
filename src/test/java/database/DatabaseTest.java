package database;

import model.Book;
import org.hibernate.Session;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void getSessionFactory() throws SQLException, ClassNotFoundException {
        Session session = Database.getInstance().getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        criteria.from(Book.class);
        List<Book> books = session.createQuery(criteria).getResultList();
        session.close();
        assertNotNull("There are no data in table books", books);
    }
}