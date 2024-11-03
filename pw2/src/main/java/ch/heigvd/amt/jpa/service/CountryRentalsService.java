package ch.heigvd.amt.jpa.service;

import ch.heigvd.amt.jpa.entity.Address_;
import ch.heigvd.amt.jpa.entity.City_;
import ch.heigvd.amt.jpa.entity.Country_;
import ch.heigvd.amt.jpa.entity.Customer_;
import ch.heigvd.amt.jpa.entity.Rental;
import ch.heigvd.amt.jpa.entity.Rental_;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Exercise Country by rentals.
 * Signature of methods (countryRentals_*) must not be changed.
 */
@ApplicationScoped
public class CountryRentalsService {

    @Inject
    private EntityManager em;

    public record CountryRentals(String country, Long rentals) {
    }

    public List<CountryRentals> countryRentals_NativeSQL() {
        var query = """
                SELECT c.country, COUNT(r.rental_id) AS rentals
                FROM country c
                    JOIN city ci ON c.country_id = ci.country_id
                    JOIN address a ON ci.city_id = a.city_id
                    JOIN customer cu ON a.address_id = cu.address_id
                    JOIN rental r ON cu.customer_id = r.customer_id
                GROUP BY c.country, c.country_id
                ORDER BY rentals DESC, c.country, c.country_id;
                """;
        List<Object[]> rawResults = em.createNativeQuery(query).getResultList();
        return rawResults.stream()
                .map(r -> new CountryRentals(
                        (String) r[0],
                        ((Number) r[1]).longValue()
                )).toList();
    }

    public List<CountryRentals> countryRentals_JPQL() {
        return em.createQuery("""
                        SELECT new ch.heigvd.amt.jpa.service.CountryRentalsService$CountryRentals(
                            c.country,
                            COUNT(r.id)
                        )
                        FROM Rental r
                            JOIN r.customer cu
                            JOIN cu.address a
                            JOIN a.city ci
                            JOIN ci.country c
                        GROUP BY c.country, c.id
                        ORDER BY COUNT(r.id) DESC, c.country, c.id
                        """,
                CountryRentals.class
        ).getResultList();
    }

    public List<CountryRentals> countryRentals_CriteriaString() {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(CountryRentals.class);
        var rentals = query.from(Rental.class);
        var country = rentals
                .join("customer")
                .join("address")
                .join("city")
                .join("country");
        query.multiselect(
                country.get("country"),
                cb.count(rentals.get("id"))
        );
        query.groupBy(country.get("country"), country.get("id"));
        query.orderBy(
                cb.desc(cb.count(rentals.get("id"))),
                cb.asc(country.get("country")),
                cb.asc(country.get("id"))
        );
        return em.createQuery(query).getResultList();
    }

    public List<CountryRentals> countryRentals_CriteriaMetaModel() {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(CountryRentals.class);
        var rentals = query.from(Rental.class);
        var country = rentals
                .join(Rental_.customer)
                .join(Customer_.address)
                .join(Address_.city)
                .join(City_.country);
        query.multiselect(
                country.get(Country_.country),
                cb.count(rentals.get(Rental_.id))
        );
        query.groupBy(country.get(Country_.country), country.get(Country_.id));
        query.orderBy(
                cb.desc(cb.count(rentals.get(Rental_.id))),
                cb.asc(country.get(Country_.country)),
                cb.asc(country.get(Country_.id))
        );
        return em.createQuery(query).getResultList();
    }
}
