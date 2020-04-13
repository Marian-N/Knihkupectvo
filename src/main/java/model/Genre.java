package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Genre {
    private int ID;
    private String name;

    public Genre(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Genre(ResultSet resultSet) throws SQLException {
        this.ID = resultSet.getInt("genre_id");
        this.name = resultSet.getString("genre_name");
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
