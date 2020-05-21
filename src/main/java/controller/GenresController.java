package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Genre;
import model.Genres;
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

public class GenresController {
    private static GenresController _instance = null;
    private Logger logger = LoggerFactory.getLogger(GenresController.class);
    private Connection connection;
    private ObservableMap<Integer, Genre> genres = FXCollections.observableHashMap();

    private GenresController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
    }

    public static GenresController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new GenresController();
        return _instance;
    }

    public Genres getBookGenres(int bookID) throws SQLException, ClassNotFoundException {
        return new Genres(bookID);
    }

    public ObservableMap<Integer, Genre> getGenres() throws SQLException {
        String query = "SELECT * from genres";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Genre genre = new Genre(id, name);
            genres.put(id, genre);
        }
        statement.close();
        resultSet.close();
        return genres;
    }

    public ObservableList<Genre> getGenre(String name) {
        Session session = Database.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Genre> query = criteriaBuilder.createQuery(Genre.class);
        Root<Genre> rootEntry = query.from(Genre.class);
        query.select(rootEntry);
        String nameLike = String.format("%c%s%c", '%', name.toLowerCase(), '%');
        query.where(criteriaBuilder.
                like(criteriaBuilder.lower(rootEntry.get("name")), nameLike));
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        List<Genre> genres = entityManager.createQuery(query).setMaxResults(20).getResultList();
        return FXCollections.observableList(genres);
    }

    public void addGenre(Genre genre) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        if(genre != null) {
            logger.info(String.format("Added genre name = %s.", genre.getName()));
            session.saveOrUpdate(genre);
            transaction.commit();
        }
        session.close();
    }
}
