package model;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Table(name = "publishers")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "name")
    private String name;

    public  Publisher() {}

    public Publisher(String name) {
        this.name = name;
    }

    public Publisher(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Publisher(ResultSet resultSet) throws SQLException {
        this.ID = resultSet.getInt(6);
        this.name = resultSet.getString(8);
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
