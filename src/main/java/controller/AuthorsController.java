package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Author;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AuthorsController {
    private Logger logger = LoggerFactory.getLogger(AuthorsController.class);
    private static AuthorsController _instance = null;
    private Connection connection;
    private ObservableMap<Integer, Author> authors = FXCollections.observableHashMap();

    private AuthorsController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
        String query = "SELECT * from authors";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Author author = new Author(id, name);
            authors.put(id, author);
        }
        statement.close();
        resultSet.close();
    }

    public static AuthorsController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new AuthorsController();
        return _instance;
    }

    public ObservableMap<Integer, Author> getAuthors() {
        return authors;
    }

    /**
     * Return all authors that match name with pattern %name%, not case sensitive
     * @param name
     * @return ObservableList of Author models
     */
    public ObservableList<Author> getAuthor(String name) {
        Session session = Database.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Author> query = criteriaBuilder.createQuery(Author.class);
        Root<Author> rootEntry = query.from(Author.class);
        query.select(rootEntry);
        String nameLike = String.format("%c%s%c", '%', name.toLowerCase(), '%');
        query.where(criteriaBuilder.
                like(criteriaBuilder.lower(rootEntry.get("name")), nameLike));
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        List<Author> authors = entityManager.createQuery(query).setMaxResults(20).getResultList();
        return FXCollections.observableList(authors);
    }

    /**
     * Add author to database
     * @param author
     */
    public void addAuthor(Author author) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        if(author != null) {
            logger.info(String.format("Added author, name = %s.", author.getName()));
            session.saveOrUpdate(author);
            transaction.commit();
        }
        session.close();
    }

    /**
     * Return author from database
     * @param ID
     * @return Author
     */
    public Author getAuthor(int ID) {
        Session session = Database.getSessionFactory().openSession();
        Author author = session.get(Author.class, ID);
        return author;
    }
}
