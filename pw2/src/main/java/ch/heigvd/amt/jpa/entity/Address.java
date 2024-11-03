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
 * This class represents an address in the Sakila database.
 */
@Entity
@Table(name = "address", indexes = {
        // ----- CITY FK INDEX
        // > CREATE INDEX idx_fk_city_id ON address
        @Index(name = "idx_fk_city_id", columnList = "city_id"),
})
public class Address {

    // ----- ID COLUMN
    // > address_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "address_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- ADDRESS COLUMN
    // > address CHARACTER VARYING(50) NOT NULL
    @Column(name = "address", length = 50, nullable = false)
    private String address;

    /**
     * @return the first address line
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the first address line
     *
     * @param address new first address line
     */
    public void setAddress(String address) {
        this.address = address;
    }

    // ----- ADDRESS2 COLUMN
    // > address2 CHARACTER VARYING(50)
    @Column(name = "address2", length = 50)
    private String address2;

    /**
     * @return the second address line
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Set the second address line
     *
     * @param address2 new second address line
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    // ----- DISTRICT COLUMN
    // > district CHARACTER VARYING(20) NOT NULL
    @Column(name = "district", length = 20, nullable = false)
    private String district;

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Set the district
     *
     * @param district new district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    // ----- CITY FOREIGN KEY
    // > city_id INTEGER NOT NULL FOREIGN KEY REFERENCES city(city_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "city_id", nullable = false, foreignKey = @ForeignKey(
            name = "address_city_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (city_id) REFERENCES city(city_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private City city;

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * Set the city
     *
     * @param city new city
     */
    public void setCity(City city) {
        this.city = city;
    }

    // ----- POSTAL_CODE COLUMN
    // > postal_code CHARACTER VARYING(10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    /**
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Set the postal code
     *
     * @param postalCode new postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    // ----- PHONE COLUMN
    // > postal_code CHARACTER VARYING(20) NOT NULL
    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    /**
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the phone number
     *
     * @param phone new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{' +
               ", address='" + address + '\'' +
               ", address2='" + address2 + '\'' +
               ", district='" + district + '\'' +
               ", city=" + city +
               ", postalCode='" + postalCode + '\'' +
               ", phone='" + phone + '\'' +
               '}';
    }

}
