package ch.heigvd.amt.jpa.entity;

import ch.heigvd.amt.jpa.entity.enums.Rating;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestTransaction
public class FilmMappingTest {

    @Inject
    EntityManager em;

    @Test
    public void testFilmRatingMapping() {
        // Find a film with each rating type to ensure mapping works
        for (Rating rating : Rating.values()) {
            Film film = em.createQuery(
                            "SELECT f FROM Film f WHERE f.rating = :rating",
                            Film.class)
                    .setParameter("rating", rating)
                    .setMaxResults(1)
                    .getSingleResult();

            assertNotNull(film, "Should find a film with rating " + rating);
            assertEquals(rating, film.getRating(), "Rating should be correctly mapped");

            // Also verify the database value matches the expected code
            String dbValue = (String) em
                    .createNativeQuery("SELECT rating FROM film WHERE film_id = :id")
                    .setParameter("id", film.getId())
                    .getSingleResult();

            assertEquals(rating.getCode(), dbValue, "Database value should match rating code for " + rating);
        }
    }

    @Test
    public void testFilmSpecialFeaturesMapping() {
        // Find a film with multiple special features
        Film film = em
                .createQuery("SELECT f FROM Film f WHERE f.specialFeatures IS NOT NULL", Film.class)
                .setMaxResults(1)
                .getSingleResult();

        assertNotNull(film, "Should find a film with special features");
        assertNotNull(film.getSpecialFeatures(), "Special features should not be null");
        assertNotEquals(0, film.getSpecialFeatures().length, "Special features should not be empty");

        // Verify the actual database array value
        String[] dbFeatures = (String[]) em
                .createNativeQuery("SELECT special_features FROM film WHERE film_id = :id")
                .setParameter("id", film.getId())
                .getSingleResult();

        assertNotNull(dbFeatures, "Database special features should not be null");
        assertEquals(film.getSpecialFeatures().length, dbFeatures.length, "Number of special features should match");

        // Verify each feature is in the set
        String[] entityFeatures = film.getSpecialFeatures();
        for (String feature : dbFeatures) {
            assertTrue(Arrays.asList(entityFeatures).contains(feature), "Entity should contain feature: " + feature);
        }
    }

    @Test
    public void testCreateFilmWithRatingAndFeatures() {
        Film newFilm = new Film();
        newFilm.setTitle("Test Film");
        newFilm.setRating(Rating.PARENTAL_GUIDANCE_STRONGLY_SUGGESTED);
        newFilm.setSpecialFeatures(new String[]{"Trailers", "Commentaries"});

        // Need to set required relationships
        Language language = em.createQuery(
                        "SELECT l FROM Language l", Language.class)
                .setMaxResults(1)
                .getSingleResult();
        newFilm.setLanguage(language);

        // Persist and flush to ensure it's saved
        em.persist(newFilm);
        em.flush();

        // Clear the persistence context to ensure we're reading from the database
        em.clear();

        // Read the film back
        Film readFilm = em.find(Film.class, newFilm.getId());

        assertNotNull(readFilm, "Should be able to read the film back");
        assertEquals(Rating.PARENTAL_GUIDANCE_STRONGLY_SUGGESTED, readFilm.getRating(), "Rating should be persisted correctly");
        assertNotNull(readFilm.getSpecialFeatures(), "Special features should be loaded");
        assertEquals(2, readFilm.getSpecialFeatures().length, "Should have 2 special features");
        assertTrue(Arrays.asList(readFilm.getSpecialFeatures()).contains("Trailers"), "Should contain Trailers");
        assertTrue(Arrays.asList(readFilm.getSpecialFeatures()).contains("Commentaries"), "Should contain Commentaries");
    }
}
