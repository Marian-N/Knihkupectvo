package model;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "name")
    private String name;

    public Author() {}

    public Author(String name) {
        this.name = name;
    }

    public Author(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    /**
     * Create author from result set
     * @param resultSet
     * @throws SQLException
     */
    public Author(ResultSet resultSet) throws SQLException {
        this.ID = resultSet.getInt("author_id");
        this.name = resultSet.getString("author_name");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
