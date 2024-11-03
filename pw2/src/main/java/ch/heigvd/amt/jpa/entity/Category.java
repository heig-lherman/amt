package ch.heigvd.amt.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This class represents a category in the Sakila database.
 */
@Entity
@Table(name = "category")
public class Category {

    // ----- ID COLUMN
    // > category_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "category_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- NAME COLUMN
    // > first_name CHARACTER VARYING(25) NOT NULL
    @Column(name = "name", length = 25, nullable = false)
    private String name;

    /**
     * @return the name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the category.
     *
     * @param name new name of the category.
     */
    public void setName(String name) {
        this.name = name;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "name=" + name
               + '}';
    }
}
