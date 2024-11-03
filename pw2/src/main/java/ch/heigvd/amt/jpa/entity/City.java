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

/**
 * This class represents a city in the Sakila database.
 */
@Entity
@Table(name = "city", indexes = {
        // ----- COUNTRY FK INDEX
        // > idx_fk_country_id ON city(country_id)
        @Index(name = "idx_fk_country_id", columnList = "country_id"),
})
public class City {

    // ----- ID COLUMN
    // > city_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "city_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- CITY COLUMN
    // > city CHARACTER VARYING(50) NOT NULL
    @Column(name = "city", length = 50, nullable = false)
    private String city;

    /**
     * @return the city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the city name.
     *
     * @param city new city name.
     */
    public void setCity(String city) {
        this.city = city;
    }

    // ----- COUNTRY FOREIGN KEY
    // > country_id INTEGER NOT NULL FOREIGN KEY REFERENCES country(country_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(
            name = "city_country_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (country_id) REFERENCES country(country_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Country country;

    /**
     * @return the country of the city.
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Set the country of the city.
     *
     * @param country new country of the city.
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + ", city='" + city + '\''
               + ", country=" + country
               + '}';
    }
}
