package ch.heigvd.amt.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * This class represents staff in the Sakila database.
 */
@Entity
@Table(name = "staff")
public class Staff {

    // ----- ID COLUMN
    // > staff_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "staff_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- FIRST_NAME COLUMN
    // > first_name CHARACTER VARYING(45) NOT NULL
    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    /**
     * @return the first name of the staff.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the staff.
     *
     * @param firstName new first name of the staff.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // ----- LAST_NAME COLUMN
    // > last_name CHARACTER VARYING(45) NOT NULL
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    /**
     * @return the last name of the staff.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the staff.
     *
     * @param lastName new last name of the staff.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // ----- ADDRESS FOREIGN KEY
    // > address_id INTEGER NOT NULL FOREIGN KEY REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(
            name = "staff_address_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Address address;

    /**
     * @return the address of the staff.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set the address of the staff.
     *
     * @param address new address of the staff.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    // ----- FIRST_NAME COLUMN
    // > email CHARACTER VARYING(50)
    @Column(name = "email", length = 50)
    String email;

    /**
     * @return the email of the staff.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the staff.
     *
     * @param email new email of the staff.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // ----- STORE FOREIGN KEY
    // > store_id INTEGER NOT NULL FOREIGN KEY REFERENCES store(store_id)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "staff_store_id_fkey"))
    private Store store;

    /**
     * @return the store of the staff.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Set the store of the staff.
     *
     * @param store new store of the staff.
     */
    public void setStore(Store store) {
        this.store = store;
    }

    // ----- ACTIVE COLUMN
    // > active BOOLEAN NOT NULL DEFAULT true
    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT true NOT NULL")
    private boolean active = true;

    /**
     * @return the active status of the staff.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the active status of the staff.
     *
     * @param active new active status of the customer.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    // ----- USERNAME COLUMN
    // > username CHARACTER VARYING(16) NOT NULL
    @Column(name = "username", length = 16, nullable = false)
    private String username;

    /**
     * @return the username of the staff.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the staff.
     *
     * @param username new username of the staff.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    // ----- USERNAME COLUMN
    // > username CHARACTER VARYING(40)
    @Column(name = "password", length = 40)
    private String password;

    /**
     * @return the password of the staff.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the staff.
     *
     * @param password new password of the staff.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    // ----- PICTURE COLUMN
    // > picture BYTEA
    @Column(name = "picture", columnDefinition = "BYTEA")
    private byte[] picture;

    /**
     * @return the picture of the staff.
     */
    public byte[] getPicture() {
        return picture;
    }

    /**
     * Set the picture of the staff.
     *
     * @param picture new picture of the staff.
     */
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "firstName='" + firstName + '\''
               + ", lastName='" + lastName + '\''
               + ", address=" + address
               + ", email='" + email + '\''
               + ", store=" + store
               + ", active=" + active
               + ", username='" + username + '\''
               + '}';
    }
}
