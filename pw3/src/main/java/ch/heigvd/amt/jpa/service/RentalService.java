package ch.heigvd.amt.jpa.service;

import ch.heigvd.amt.jpa.entity.Customer;
import ch.heigvd.amt.jpa.entity.Inventory;
import ch.heigvd.amt.jpa.entity.Rental;
import ch.heigvd.amt.jpa.entity.Staff;
import ch.heigvd.amt.jpa.entity.Store;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Signature of existing methods must not be changed.
 */
@ApplicationScoped
public class RentalService {

    @Inject
    EntityManager em;

    // @formatter:off

    // The following records must not be changed
    public record RentalDTO(Integer inventory, Integer customer) {}
    public record FilmInventoryDTO(String title, String description, Integer inventoryId) {}
    public record CustomerDTO(Integer id, String firstName, String lastName) {}

    // @formatter:on

    /**
     * Rent a film out of store's inventory for a given customer.
     *
     * @param inventory the inventory to rent
     * @param customer  the customer to which the inventory is rented
     * @param staff     the staff that process the customer's request in the store
     * @return an Optional that is present if rental is successful, if Optional is empty rental failed
     */
    @Transactional
    public Optional<RentalDTO> rentFilm(Inventory inventory, Customer customer, Staff staff) {
        try {
            // 1. Update the transaction lock level to SERIALIZABLE for READ and WRITE
            em.createNativeQuery("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE READ WRITE").executeUpdate();
            // 2. Lock additions to entities having direct foreign keys on the inventory item, this
            // will prevent all other attemps at editing rows that are linked via foreign keys to
            // the inventory item until the transaction is committed. In theory, the transactio
            // level serializable should be enough to prevent concurrent writes, but this is a belt
            // and suspenders approach.
            em.createNativeQuery("SELECT 1 FROM inventory i WHERE i.inventory_id = :inventoryId FOR UPDATE")
                .setParameter("inventoryId", inventory.getId())
                .getFirstResult();

            // 3. Ensure that there is no existing active rental for the inventory item
            var activeRentals = em.createQuery(
                "SELECT COUNT(r) FROM rental r WHERE r.inventory.id = :inventoryId AND r.returnDate IS NULL",
                Long.class
            ).setParameter("inventoryId", inventory.getId()).getSingleResult();

            if (activeRentals > 0) {
                return Optional.empty();
            }

            // 4. Insert the new rental
            var rental = new Rental();
            rental.setInventory(inventory);
            rental.setCustomer(customer);
            rental.setStaff(staff);
            rental.setRentalDate(new Timestamp(System.currentTimeMillis()));

            em.persist(rental);

            // 5. Return the rental result, releasing locks and committing the transaction
            return Optional.of(new RentalDTO(inventory.getId(), customer.getId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * @param query the searched string
     * @return films matching the query
     */
    @SuppressWarnings("unchecked")
    public List<FilmInventoryDTO> searchFilmInventory(String query, Store store) {
        var words = query.toLowerCase().trim();
        if (words.isEmpty()) {
            return Collections.emptyList();
        }

        return em.createNativeQuery(
                """
                    SELECT f.title, f.description, i.inventory_id
                    FROM inventory i
                    JOIN film f ON i.film_id = f.film_id
                    WHERE i.store_id = :storeId
                    AND (
                        to_tsvector('english', f.title) || 
                        to_tsvector('english', COALESCE(f.description, '')) || 
                        to_tsvector('simple', i.inventory_id::TEXT) @@
                        plainto_tsquery('english', :searchTerms)
                    )
                    """,
                FilmInventoryDTO.class
            )
            .setParameter("storeId", store.getId())
            .setParameter("searchTerms", words)
            .getResultList();
    }

    public FilmInventoryDTO searchFilmInventory(Integer inventoryId) {
        return em.createQuery(
            """
                select new ch.heigvd.amt.jpa.service.RentalService$FilmInventoryDTO(i.film.title, i.film.description, i.id)
                from inventory i
                where i.id = :inventoryId
                """,
            FilmInventoryDTO.class
        ).setParameter("inventoryId", inventoryId).getSingleResult();
    }

    public CustomerDTO searchCustomer(Integer customerId) {
        return Optional
            .ofNullable(em.find(Customer.class, customerId))
            .map(c -> new CustomerDTO(c.getId(), c.getFirstName(), c.getLastName()))
            .orElse(null);
    }

    @SuppressWarnings("unchecked")
    public List<CustomerDTO> searchCustomer(String query, Store store) {
        var words = query.toLowerCase().trim();
        if (words.isEmpty()) {
            return Collections.emptyList();
        }

        return em.createNativeQuery(
                """
                    SELECT c.customer_id, c.first_name, c.last_name
                    FROM customer c
                    WHERE c.store_id = :storeId
                    AND (
                        to_tsvector('simple', COALESCE(c.first_name, '')) || 
                        to_tsvector('simple', COALESCE(c.last_name, '')) ||
                        to_tsvector('simple', c.customer_id::TEXT) @@
                        plainto_tsquery('simple', :searchTerms)
                    )
                    """,
                CustomerDTO.class
            )
            .setParameter("storeId", store.getId())
            .setParameter("searchTerms", words)
            .getResultList();
    }
}
