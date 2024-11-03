package ch.heigvd.amt.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * This class represents a customer in the Sakila database.
 */
@Entity
@Table(name = "customer", indexes = {
        // ----- LAST_NAME INDEX
        // > idx_last_name ON customer(last_name)
        @Index(name = "idx_last_name", columnList = "last_name"),
        // ----- ADDRESS FK INDEX
        // > idx_fk_address_id ON customer(address_id)
        @Index(name = "idx_fk_address_id", columnList = "address_id"),
        // ----- STORE FK INDEX
        // > idx_fk_store_id ON customer(store_id)
        @Index(name = "idx_fk_store_id", columnList = "store_id"),
})
public class Customer {

    // ----- ID COLUMN
    // > customer_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "customer_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- STORE FOREIGN KEY
    // > store_id INTEGER NOT NULL FOREIGN KEY REFERENCES store(store_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(
            name = "customer_store_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (store_id) REFERENCES store(store_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Store store;

    /**
     * @return the store of the customer.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Set the store of the customer.
     *
     * @param store new store of the customer.
     */
    public void setStore(Store store) {
        this.store = store;
    }

    // ----- FIRST_NAME COLUMN
    // > first_name CHARACTER VARYING(45) NOT NULL
    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    /**
     * @return the first name of the customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the customer.
     *
     * @param firstName new first name of the customer.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // ----- LAST_NAME COLUMN
    // > last_name CHARACTER VARYING(45) NOT NULL
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    /**
     * @return the last name of the customer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the customer.
     *
     * @param lastName new last name of the customer.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // ----- EMAIL COLUMN
    // > email CHARACTER VARYING(50)
    @Column(name = "email", length = 50)
    private String email;

    /**
     * @return the email of the customer.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the customer.
     *
     * @param email new email of the customer.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    // ----- ADDRESS FOREIGN KEY
    // > address_id INTEGER NOT NULL FOREIGN KEY REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(
            name = "customer_address_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Address address;

    /**
     * @return the address of the customer.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set the address of the customer.
     *
     * @param address new address of the customer.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    // ----- ACTIVE COLUMN
    // > activebool BOOLEAN NOT NULL DEFAULT true
    @Column(name = "activebool", nullable = false, columnDefinition = "BOOLEAN DEFAULT true NOT NULL")
    private boolean active = true;

    // > active INTEGER
    @Column(name = "active")
    private Integer activeNumeric;

    /**
     * @return the active status of the customer.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the active status of the customer.
     *
     * @param active new active status of the customer.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the active status of the customer.
     */
    public Integer getActiveNumeric() {
        return activeNumeric;
    }

    /**
     * Set the active status of the customer.
     *
     * @param activeNumeric new active status of the customer.
     */
    public void setActiveNumeric(Integer activeNumeric) {
        this.activeNumeric = activeNumeric;
    }

    // ----- CREATE_DATE COLUMN
    // > create_date DATE NOT NULL DEFAULT ('now'::text)::date
    @Column(
            name = "create_date",
            updatable = false,
            nullable = false,
            columnDefinition = "DATE DEFAULT ('now'::text)::date NOT NULL"
    )
    private LocalDate createDate;

    /**
     * @return the creation date of the customer.
     */
    public LocalDate getCreateDate() {
        return createDate;
    }

    /**
     * Set the creation date of the customer.
     *
     * @param createDate new creation date of the customer.
     */
    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "store=" + store
               + ", firstName='" + firstName + '\''
               + ", lastName='" + lastName + '\''
               + ", email='" + email + '\''
               + ", address=" + address
               + ", active=" + active
               + ", activeNumeric=" + activeNumeric
               + ", createDate=" + createDate
               + '}';
    }
}
