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
 * This class represents a store in the Sakila database.
 */
@Entity
@Table(name = "store", indexes = {
        // ----- MANAGER_STAFF INDEX
        // > idx_unq_manager_staff_id UNIQUE ON store(manager_staff_id)
        @Index(name = "idx_unq_manager_staff_id", unique = true, columnList = "manager_staff_id")
})
public class Store {

    // ----- ID COLUMN
    // > store_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "store_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- MANAGER FOREIGN KEY
    // > manager_staff_id INTEGER NOT NULL FOREIGN KEY REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "manager_staff_id", nullable = false, foreignKey = @ForeignKey(
            name = "store_manager_staff_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (manager_staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Staff manager;

    /**
     * @return the manager of the store.
     */
    public Staff getManager() {
        return manager;
    }

    /**
     * Set the manager of the store.
     *
     * @param manager new manager of the store.
     */
    public void setManager(Staff manager) {
        this.manager = manager;
    }

    // ----- ADDRESS FOREIGN KEY
    // > address_id INTEGER NOT NULL FOREIGN KEY REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "address_id", nullable = false, foreignKey = @ForeignKey(
            name = "store_address_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Address address;

    /**
     * @return the address of the store.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set the address of the store.
     *
     * @param address new address of the store.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "manager=" + manager
               + ", address=" + address
               + '}';
    }
}
