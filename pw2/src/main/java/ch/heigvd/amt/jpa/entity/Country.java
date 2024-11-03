package ch.heigvd.amt.jpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This class represents a country in the Sakila database.
 */
@Entity
@Table(name = "country")
public class Country {

    // ----- ID COLUMN
    // > country_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "country_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- COUNTRY COLUMN
    // > country CHARACTER VARYING(50) NOT NULL
    @Column(name = "country", length = 50, nullable = false)
    private String country;

    /**
     * @return the country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the country name.
     *
     * @param country new country name.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "country=" + country
               + '}';
    }
}
