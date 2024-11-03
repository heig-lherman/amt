package ch.heigvd.amt.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This class represents a language in the Sakila database.
 */
@Entity
@Table(name = "language")
public class Language {

    // ----- ID COLUMN
    // > language_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "language_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- NAME COLUMN
    // > name VARCHAR(20) NOT NULL
    @Column(name = "name", length = 20, nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
    private String name;

    /**
     * @return the name of the language.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the language.
     *
     * @param name new name of the language.
     */
    public void setName(String name) {
        this.name = name;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + ", name='" + name + '\''
               + '}';
    }
}
