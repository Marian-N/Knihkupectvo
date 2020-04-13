package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Author {
    private int ID;
    private String name;

    public Author(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

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
