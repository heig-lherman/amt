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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * This class represents a payment in the Sakila database.
 */
@Entity
@Table(name = "payment", indexes = {
        // ----- CUSTOMER FK INDEX
        // > idx_fk_customer_id ON payment(customer_id)
        @Index(name = "idx_fk_customer_id", columnList = "customer_id"),
        // ----- STAFF FK INDEX
        // > idx_fk_staff_id ON payment(staff_id)
        @Index(name = "idx_fk_staff_id", columnList = "staff_id"),
})
public class Payment {

    // ----- ID COLUMN
    // > payment_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "payment_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- CUSTOMER FOREIGN KEY
    // > customer_id INTEGER NOT NULL FOREIGN KEY REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(
            name = "payment_customer_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Customer customer;

    /**
     * @return the customer of the payment.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Set the customer of the payment.
     *
     * @param customer new customer of the payment.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // ----- STAFF FOREIGN KEY
    // > staff_id INTEGER NOT NULL FOREIGN KEY REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "staff_id", nullable = false, foreignKey = @ForeignKey(
            name = "payment_staff_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Staff staff;

    /**
     * @return the staff of the payment.
     */
    public Staff getStaff() {
        return staff;
    }

    /**
     * Set the staff of the payment.
     *
     * @param staff new staff of the payment.
     */
    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    // ----- RENTAL FOREIGN KEY
    // > rental_id INTEGER NOT NULL FOREIGN KEY REFERENCES rental(rental_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "rental_id", nullable = false, foreignKey = @ForeignKey(
            name = "payment_rental_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (rental_id) REFERENCES rental(rental_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Rental rental;

    /**
     * @return the rental of the payment.
     */
    public Rental getRental() {
        return rental;
    }

    /**
     * Set the rental of the payment.
     *
     * @param rental new rental of the payment.
     */
    public void setRental(Rental rental) {
        this.rental = rental;
    }

    // ----- AMOUNT COLUMN
    // > amount NUMERIC(5, 2) NOT NULL
    @Column(
            name = "amount",
            precision = 5, scale = 2,
            nullable = false,
            columnDefinition = "NUMERIC(5, 2) NOT NULL"
    )
    private BigDecimal amount;

    /**
     * @return the amount of the payment.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set the amount of the payment.
     *
     * @param amount new amount of the payment.
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // ----- PAYMENT_DATE COLUMN
    // > payment_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
    @Column(name = "payment_date", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE NOT NULL")
    private LocalDateTime paymentDate;

    /**
     * @return the payment date.
     */
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    /**
     * Set the payment date.
     *
     * @param paymentDate new payment date.
     */
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + "customer=" + customer
               + ", staff=" + staff
               + ", rental=" + rental
               + ", amount=" + amount
               + ", paymentDate=" + paymentDate
               + '}';
    }
}
