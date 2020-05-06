package model;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int ID;

    @Column(name = "name")
    private String name;

    public Genre() {}

    public Genre(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Genre(ResultSet resultSet) throws SQLException {
        this.ID = resultSet.getInt(1);
        this.name = resultSet.getString(2);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
