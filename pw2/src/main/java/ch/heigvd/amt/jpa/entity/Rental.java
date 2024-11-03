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
import java.time.LocalDateTime;


/**
 * This class represents a rental in the Sakila database.
 */
@Entity
@Table(name = "rental", indexes = {
        // ----- RENTAL, INVENTORY, CUSTOMER INDEX
        // > idx_unq_rental_rental_date_inventory_id_customer_id ON rental(rental_date, inventory_id, customer_id)
        @Index(
                name = "idx_unq_rental_rental_date_inventory_id_customer_id",
                columnList = "rental_date, inventory_id, customer_id",
                unique = true
        ),
        // ----- INVENTORY FK INDEX
        // > idx_fk_inventory_id ON rental(inventory_id)
        @Index(name = "idx_fk_inventory_id", columnList = "inventory_id"),
})
public class Rental {

    // ----- ID COLUMN
    // > rental_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "rental_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- RENTAL_DATE COLUMN
    // > rental_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
    @Column(name = "rental_date", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE NOT NULL")
    private LocalDateTime rentalDate;

    /**
     * @return the rental date.
     */
    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    /**
     * Set the rental date.
     *
     * @param rentalDate new rental date.
     */
    public void setRentalDate(LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    // ----- RETURN_DATE COLUMN
    // > return_date TIMESTAMP WITHOUT TIME ZONE
    @Column(name = "return_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE NOT NULL")
    private LocalDateTime returnDate;

    /**
     * @return the return date.
     */
    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    /**
     * Set the return date.
     *
     * @param returnDate new return date.
     */
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    // ----- INVENTORY FOREIGN KEY
    // > inventory_id INTEGER NOT NULL FOREIGN KEY REFERENCES inventory(inventory_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false, foreignKey = @ForeignKey(
            name = "rental_inventory_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Inventory inventory;

    /**
     * @return the inventory of the rental.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Set the inventory of the rental.
     *
     * @param inventory new inventory of the rental.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // ----- CUSTOMER FOREIGN KEY
    // > customer_id INTEGER NOT NULL FOREIGN KEY REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(
            name = "rental_customer_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Customer customer;

    /**
     * @return the customer of the rental.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Set the customer of the rental.
     *
     * @param customer new customer of the rental.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // ----- STAFF FOREIGN KEY
    // > staff_id INTEGER NOT NULL FOREIGN KEY REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "staff_id", nullable = false, foreignKey = @ForeignKey(
            name = "rental_staff_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Staff staff;

    /**
     * @return the staff of the rental.
     */
    public Staff getStaff() {
        return staff;
    }

    /**
     * Set the staff of the rental.
     *
     * @param staff new staff of the rental.
     */
    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "rentalDate=" + rentalDate
               + ", returnDate=" + returnDate
               + ", inventory=" + inventory
               + ", customer=" + customer
               + ", staff=" + staff
               + '}';
    }
}
