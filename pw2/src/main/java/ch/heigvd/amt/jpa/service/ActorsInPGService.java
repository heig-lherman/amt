package ch.heigvd.amt.jpa.service;

import ch.heigvd.amt.jpa.entity.Actor_;
import ch.heigvd.amt.jpa.entity.Film;
import ch.heigvd.amt.jpa.entity.Film_;
import ch.heigvd.amt.jpa.entity.enums.Rating;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Exercise Actors with films of PG rating.
 * Signature of methods (actorInPGRatings_*) must not be changed.
 */
@ApplicationScoped
public class ActorsInPGService {

    @Inject
    private EntityManager em;

    public record ActorInPGRating(String firstName, String lastName, Long nbFilms) {
    }

    public List<ActorInPGRating> actorInPGRatings_NativeSQL() {
        var query = """
                SELECT a.actor_id, a.first_name, a.last_name, COUNT(f.film_id) AS nb_films
                FROM actor a
                         JOIN film_actor fa ON a.actor_id = fa.actor_id
                         JOIN film f ON fa.film_id = f.film_id
                WHERE f.rating = 'PG'
                GROUP BY a.first_name, a.last_name, a.actor_id
                ORDER BY nb_films DESC, a.first_name, a.last_name, a.actor_id;
                """;
        List<Object[]> rawResults = em.createNativeQuery(query).getResultList();
        return rawResults.stream().map(r -> new ActorInPGRating(
                (String) r[1],
                (String) r[2],
                ((Number) r[3]).longValue()
        )).toList();
    }

    public List<ActorInPGRating> actorInPGRatings_JPQL() {
        return em.createQuery(
                """
                        SELECT new ch.heigvd.amt.jpa.service.ActorsInPGService$ActorInPGRating(
                            a.firstName,
                            a.lastName,
                            COUNT(f.id)
                        )
                        FROM Film f
                            JOIN f.actors a
                        WHERE f.rating = ch.heigvd.amt.jpa.entity.enums.Rating.PARENTAL_GUIDANCE_SUGGESTED
                        GROUP BY a.firstName, a.lastName, a.id
                        ORDER BY COUNT(f.id) DESC, a.firstName, a.lastName, a.id
                        """,
                ActorInPGRating.class
        ).getResultList();
    }


    public List<ActorInPGRating> actorInPGRatings_CriteriaString() {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(ActorInPGRating.class);
        var films = query.from(Film.class);
        var actors = films.join("actors");
        query.multiselect(
                actors.get("firstName"),
                actors.get("lastName"),
                cb.count(films.get("id"))
        );
        query.where(cb.equal(films.get("rating"), Rating.PARENTAL_GUIDANCE_SUGGESTED));
        query.groupBy(
                actors.get("firstName"),
                actors.get("lastName"),
                actors.get("id")
        );
        query.orderBy(
                cb.desc(cb.count(films.get("id"))),
                cb.asc(actors.get("firstName")),
                cb.asc(actors.get("lastName")),
                cb.asc(actors.get("id"))
        );

        return em.createQuery(query).getResultList();
    }

    public List<ActorInPGRating> actorInPGRatings_CriteriaMetaModel() {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(ActorInPGRating.class);
        var films = query.from(Film.class);
        var actors = films.join(Film_.actors);
        query.multiselect(
                actors.get(Actor_.firstName),
                actors.get(Actor_.lastName),
                cb.count(films.get(Film_.id))
        );
        query.where(cb.equal(films.get(Film_.rating), Rating.PARENTAL_GUIDANCE_SUGGESTED));
        query.groupBy(
                actors.get(Actor_.firstName),
                actors.get(Actor_.lastName),
                actors.get(Actor_.id)
        );
        query.orderBy(
                cb.desc(cb.count(films.get(Film_.id))),
                cb.asc(actors.get(Actor_.firstName)),
                cb.asc(actors.get(Actor_.lastName)),
                cb.asc(actors.get(Actor_.id))
        );

        return em.createQuery(query).getResultList();
    }
}
