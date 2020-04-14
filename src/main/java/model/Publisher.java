package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Publisher {
    private int ID;
    private String name;

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
