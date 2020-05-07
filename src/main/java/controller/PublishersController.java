package controller;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Publisher;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PublishersController {
    private static PublishersController _instance = null;
    private Connection connection;
    private ObservableMap<Integer, Publisher> publishers = FXCollections.observableHashMap();

    private PublishersController() throws SQLException, ClassNotFoundException {
        connection = Database.getInstance().getConnection();
        String query = "SELECT * from publishers";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Publisher publisher = new Publisher(id, name);
            publishers.put(id, publisher);
        }
        statement.close();
        resultSet.close();
    }

    public static PublishersController getInstance() throws SQLException, ClassNotFoundException {
        if(_instance == null)
            _instance = new PublishersController();
        return _instance;
    }

    public ObservableMap<Integer, Publisher> getPublishers() {
        return publishers;
    }

    public ObservableList<Publisher> getPublisher(String name) {
        Session session = Database.getSessionFactory().openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Publisher> query = criteriaBuilder.createQuery(Publisher.class);
        Root<Publisher> rootEntry = query.from(Publisher.class);
        query.select(rootEntry);
        String nameLike = String.format("%c%s%c", '%', name.toLowerCase(), '%');
        query.where(criteriaBuilder.
                like(criteriaBuilder.lower(rootEntry.get("name")), nameLike));
        EntityManager entityManager = session.getEntityManagerFactory().createEntityManager();
        List<Publisher> publishers = entityManager.createQuery(query).setMaxResults(20).getResultList();
        return FXCollections.observableList(publishers);
    }

    public void addPublisher(Publisher publisher) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        if(publisher != null) {
            session.saveOrUpdate(publisher);
            transaction.commit();
        }
        session.close();
    }
}
