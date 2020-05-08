package model;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mail")
    private String mail;

    @Column(name = "city")
    private String city;

    @Column(name = "zip")
    private String zip;

    @Column(name = "address")
    private String address;

    @Column(name = "role")
    private int role;

    @Column(name = "password")
    private String encryptedPassword;

    public Customer() {}

    public Customer(String firstName, String lastName, String mail, String city, String zip, String address, int role, String encryptedPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.city = city;
        this.zip = zip;
        this.address = address;
        this.role = role;
        this.encryptedPassword = encryptedPassword;
    }

    public Customer(int ID, String firstName, String lastName, String mail, String city, String zip, String address) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.city = city;
        this.zip = zip;
        this.address = address;
    }

    public Customer(ResultSet resultSet) throws SQLException {
        resultSet.next();
        ID = resultSet.getInt("id");
        firstName = resultSet.getString("first_name");
        lastName = resultSet.getString("last_name");
        mail = resultSet.getString("mail");
        city = resultSet.getString("city");
        zip = resultSet.getString("zip");
        address = resultSet.getString("address");

        resultSet.close();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
