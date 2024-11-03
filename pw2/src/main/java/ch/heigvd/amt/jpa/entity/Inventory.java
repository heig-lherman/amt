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
 * This class represents an inventory in the Sakila database.
 */
@Entity
@Table(name = "inventory", indexes = {
        // ----- STORE_ID, FILM_ID INDEX
        // > idx_store_id_film_id ON inventory(store_id, film_id)
        @Index(name = "idx_store_id_film_id", columnList = "store_id, film_id")
})
public class Inventory {

    // ----- ID COLUMN
    // > inventory_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "inventory_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- FILM FOREIGN KEY
    // > film_id INTEGER NOT NULL FOREIGN KEY REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "film_id", nullable = false, foreignKey = @ForeignKey(
            name = "inventory_film_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Film film;

    /**
     * @return the film of the inventory.
     */
    public Film getFilm() {
        return film;
    }

    /**
     * Set the film of the inventory.
     *
     * @param film new film of the inventory.
     */
    public void setFilm(Film film) {
        this.film = film;
    }

    // ----- STORE FOREIGN KEY
    // > store_id INTEGER NOT NULL FOREIGN KEY REFERENCES store(store_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(
            name = "inventory_store_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (store_id) REFERENCES store(store_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Store store;

    /**
     * @return the store of the inventory.
     */
    public Store getStore() {
        return store;
    }

    /**
     * Set the store of the inventory.
     *
     * @param store new store of the inventory.
     */
    public void setStore(Store store) {
        this.store = store;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "film=" + film
               + ", store=" + store
               + '}';
    }
}
