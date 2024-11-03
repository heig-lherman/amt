package ch.heigvd.amt.jpa.entity;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
@TestTransaction
public class EntitiesTest {

    @Inject
    private EntityManager em;

    @Test
    public void testSelectAllEntities() {
        // Test Actor entity
        var actors = em.createQuery("SELECT a FROM Actor a", Actor.class).getResultList();
        assertFalse(actors.isEmpty(), "Should have actors in database");

        // Test Address entity
        var addresses = em.createQuery("SELECT a FROM Address a", Address.class).getResultList();
        assertFalse(addresses.isEmpty(), "Should have addresses in database");

        // Test Category entity
        var categories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        assertFalse(categories.isEmpty(), "Should have categories in database");

        // Test City entity
        var cities = em.createQuery("SELECT c FROM City c", City.class).getResultList();
        assertFalse(cities.isEmpty(), "Should have cities in database");

        // Test Country entity
        var countries = em.createQuery("SELECT c FROM Country c", Country.class).getResultList();
        assertFalse(countries.isEmpty(), "Should have countries in database");

        // Test Customer entity
        var customers = em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        assertFalse(customers.isEmpty(), "Should have customers in database");

        // Test Film entity
        var films = em.createQuery("SELECT f FROM Film f", Film.class).getResultList();
        assertFalse(films.isEmpty(), "Should have films in database");

        // Test Film_Actor entity
        var filmActors = em.createNativeQuery("SELECT * FROM FILM_ACTOR").getResultList();
        assertFalse(filmActors.isEmpty(), "Should have film_actor relationships in database");

        // Test Film_Category entity
        var filmCategories = em.createNativeQuery("SELECT * FROM FILM_CATEGORY").getResultList();
        assertFalse(filmCategories.isEmpty(), "Should have film_category relationships in database");

        // Test Inventory entity
        var inventory = em.createQuery("SELECT i FROM Inventory i", Inventory.class).getResultList();
        assertFalse(inventory.isEmpty(), "Should have inventory items in database");

        // Test Language entity
        var languages = em.createQuery("SELECT l FROM Language l", Language.class).getResultList();
        assertFalse(languages.isEmpty(), "Should have languages in database");

        // Test Payment entity
        var payments = em.createQuery("SELECT p FROM Payment p", Payment.class).getResultList();
        assertFalse(payments.isEmpty(), "Should have payments in database");

        // Test Rental entity
        var rentals = em.createQuery("SELECT r FROM Rental r", Rental.class).getResultList();
        assertFalse(rentals.isEmpty(), "Should have rentals in database");

        // Test Staff entity
        var staff = em.createQuery("SELECT s FROM Staff s", Staff.class).getResultList();
        assertFalse(staff.isEmpty(), "Should have staff members in database");

        // Test Store entity
        var stores = em.createQuery("SELECT s FROM Store s", Store.class).getResultList();
        assertFalse(stores.isEmpty(), "Should have stores in database");

        System.out.println("Database statistics:");
        System.out.println("- Actors: " + actors.size());
        System.out.println("- Addresses: " + addresses.size());
        System.out.println("- Categories: " + categories.size());
        System.out.println("- Cities: " + cities.size());
        System.out.println("- Countries: " + countries.size());
        System.out.println("- Customers: " + customers.size());
        System.out.println("- Films: " + films.size());
        System.out.println("- Film-Actor relationships: " + filmActors.size());
        System.out.println("- Film-Category relationships: " + filmCategories.size());
        System.out.println("- Inventory items: " + inventory.size());
        System.out.println("- Languages: " + languages.size());
        System.out.println("- Payments: " + payments.size());
        System.out.println("- Rentals: " + rentals.size());
        System.out.println("- Staff members: " + staff.size());
        System.out.println("- Stores: " + stores.size());

        assertEquals(200, actors.size());
        assertEquals(603, addresses.size());
        assertEquals(16, categories.size());
        assertEquals(600, cities.size());
        assertEquals(109, countries.size());
        assertEquals(599, customers.size());
        assertEquals(1000, films.size());
        assertEquals(5462, filmActors.size());
        assertEquals(1000, filmCategories.size());
        assertEquals(4581, inventory.size());
        assertEquals(6, languages.size());
        assertEquals(16049, payments.size());
        assertEquals(16044, rentals.size());
        assertEquals(2, staff.size());
        assertEquals(2, stores.size());
    }
}
