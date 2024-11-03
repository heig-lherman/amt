package ch.heigvd.amt.jpa.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * This class represents an actor in the Sakila database. In this example, we use the attributes of the JPA annotations
 * to correctly map the table name, the sequence name, the column names, and the column types. Use the annotations
 * extensively to correctly map the database schema to the Java entities.
 */
@Entity
@Table(name = "actor", indexes = {
        // ----- LAST_NAME INDEX
        // > idx_actor_last_name ON actor(last_name)
        @Index(name = "idx_actor_last_name", columnList = "last_name")
})
public class Actor {

    // ----- ID COLUMN
    // > actor_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "actor_id", insertable = false, updatable = false, nullable = false)
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
     * @return the first name of the actor.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the actor.
     *
     * @param firstName new first name of the actor.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // ----- LAST_NAME COLUMN
    // > last_name CHARACTER VARYING(45) NOT NULL
    @Basic(optional = false)
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    /**
     * @return the last name of the actor.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the actor.
     *
     * @param lastName new last name of the actor.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "firstname=" + firstName
               + ", lastname=" + lastName
               + '}';
    }
}
