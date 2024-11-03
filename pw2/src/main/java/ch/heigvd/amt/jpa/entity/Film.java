package ch.heigvd.amt.jpa.entity;


import ch.heigvd.amt.jpa.entity.enums.Rating;
import ch.heigvd.amt.jpa.entity.type.RatingType;
import ch.heigvd.amt.jpa.entity.type.StringArrayType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Type;

/**
 * This class represents a film in the Sakila database.
 */
@Entity
@Table(name = "film", indexes = {
        // ----- FILM TITLE INDEX
        // > idx_title ON film(title)
        @Index(name = "idx_title", columnList = "title"),
        // ----- LANGUAGE FK INDEX
        // > idx_fk_language_id ON film(language_id)
        @Index(name = "idx_fk_language_id", columnList = "language_id"),
        // ----- ORIGINAL LANGUAGE FK INDEX
        // > idx_fk_original_language_id ON film(original_language_id)
        @Index(name = "idx_fk_original_language_id", columnList = "original_language_id"),
})
public class Film {

    // ----- ID COLUMN
    // > film_id INTEGER NOT NULL UNIQUE GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    @Id
    @Column(name = "film_id", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * @return ID attribute value.
     */
    public int getId() {
        return id;
    }

    // ----- TITLE COLUMN
    // > title CHARACTER VARYING(255) NOT NULL
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * @return the title of the film.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the film.
     *
     * @param title new title of the film.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    // ----- DESCRIPTION COLUMN
    // > description TEXT
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * @return the description of the film.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the film.
     *
     * @param description new description of the film.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // ----- RELEASE_YEAR COLUMN
    // > release_year INTEGER
    @Column(name = "release_year")
    private Year releaseYear;

    /**
     * @return the release year of the film.
     */
    public Year getReleaseYear() {
        return releaseYear;
    }

    /**
     * Set the release year of the film.
     *
     * @param releaseYear new release year of the film.
     */
    public void setReleaseYear(Year releaseYear) {
        this.releaseYear = releaseYear;
    }

    // ----- LANGUAGE FOREIGN KEY
    // > language_id INTEGER NOT NULL FOREIGN KEY REFERENCES language(language_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false, foreignKey = @ForeignKey(
            name = "film_language_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (language_id) REFERENCES language(language_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Language language;

    /**
     * @return the language of the film.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Set the language of the film.
     *
     * @param language new language of the film.
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    // ----- ORIGINAL_LANGUAGE FOREIGN KEY
    // > original_language_id INTEGER FOREIGN KEY REFERENCES language(language_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "original_language_id", foreignKey = @ForeignKey(
            name = "film_original_language_id_fkey",
            foreignKeyDefinition = "FOREIGN KEY (original_language_id) REFERENCES language(language_id) ON UPDATE CASCADE ON DELETE RESTRICT"
    ))
    private Language originalLanguage;

    /**
     * @return the original language of the film.
     */
    public Language getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * Set the original language of the film.
     *
     * @param originalLanguage new original language of the film.
     */
    public void setOriginalLanguage(Language originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    // ----- RENTAL_DURATION COLUMN
    // > rental_duration SMALLINT NOT NULL DEFAULT 3
    @Column(name = "rental_duration", nullable = false, columnDefinition = "SMALLINT DEFAULT 3 NOT NULL")
    private short rentalDuration = 3;

    /**
     * @return the rental duration of the film.
     */
    public short getRentalDuration() {
        return rentalDuration;
    }

    /**
     * Set the rental duration of the film.
     *
     * @param rentalDuration new rental duration of the film.
     */
    public void setRentalDuration(short rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    // ----- RENTAL_RATE COLUMN
    // > rental_rate NUMERIC(4, 2) NOT NULL DEFAULT 4.99
    @Column(
            name = "rental_rate",
            precision = 4, scale = 2,
            nullable = false,
            columnDefinition = "NUMERIC(4, 2) DEFAULT 4.99 NOT NULL"
    )
    private BigDecimal rentalRate = BigDecimal.valueOf(4.99);

    /**
     * @return the rental rate of the film.
     */
    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    /**
     * Set the rental rate of the film.
     *
     * @param rentalRate new rental rate of the film.
     */
    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    // ----- LENGTH COLUMN
    // > length SMALLINT
    @Column(name = "length", columnDefinition = "SMALLINT")
    private short length;

    /**
     * @return the length of the film.
     */
    public short getLength() {
        return length;
    }

    /**
     * Set the length of the film.
     *
     * @param length new length of the film.
     */
    public void setLength(short length) {
        this.length = length;
    }

    // ----- REPLACEMENT_COST COLUMN
    // > replacement_cost NUMERIC(5, 2) NOT NULL DEFAULT 19.99
    @Column(
            name = "replacement_cost",
            precision = 5, scale = 2,
            nullable = false,
            columnDefinition = "NUMERIC(5, 2) DEFAULT 19.99 NOT NULL"
    )
    private BigDecimal replacementCost = BigDecimal.valueOf(19.99);

    /**
     * @return the replacement cost of the film.
     */
    public BigDecimal getReplacementCost() {
        return replacementCost;
    }

    /**
     * Set the replacement cost of the film.
     *
     * @param replacementCost new replacement cost of the film.
     */
    public void setReplacementCost(BigDecimal replacementCost) {
        this.replacementCost = replacementCost;
    }

    // ----- RATING COLUMN
    // > rating MPAA_RATING ENUM('G', 'PG', 'PG-13', 'R', 'NC-17') DEFAULT 'G'
    @Type(value = RatingType.class)
    @Column(name = "rating", columnDefinition = "mpaa_rating DEFAULT 'G'::mpaa_rating")
    private Rating rating;

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    // ----- SPECIAL_FEATURES COLUMN
    // > special_features TEXT[]
    @Type(StringArrayType.class)
    @Column(name = "special_features", columnDefinition = "text[]")
    private String[] specialFeatures;

    /**
     * @return the special features of the film.
     */
    public String[] getSpecialFeatures() {
        return specialFeatures;
    }

    /**
     * Set the special features of the film.
     *
     * @param specialFeatures new special features of the film.
     */
    public void setSpecialFeatures(String[] specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    // ----- FILM ACTORS MANY-TO-MANY
    // > film_actor(film_id, actor_id) FOREIGN KEY(film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id", nullable = false, foreignKey = @ForeignKey(
                    name = "film_actor_film_id_fkey",
                    foreignKeyDefinition = "FOREIGN KEY (film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT"
            )),
            inverseJoinColumns = @JoinColumn(name = "actor_id", nullable = false, foreignKey = @ForeignKey(
                    name = "film_actor_actor_id_fkey",
                    foreignKeyDefinition = "FOREIGN KEY (actor_id) REFERENCES actor(actor_id) ON UPDATE CASCADE ON DELETE RESTRICT"
            )),
            indexes = {
                    // ----- FILM_ACTOR FILM FK INDEX
                    // > idx_film_actor_film_id ON film_actor(film_id)
                    @Index(name = "idx_fk_film_id", columnList = "film_id"),
            }
    )
    private List<Actor> actors;

    /**
     * @return the list of actors for this film
     */
    public List<Actor> getActors() {
        return actors;
    }

    /**
     * Set the list of actors for this film.
     *
     * @param actors the list of actors.
     */
    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    /**
     * Add an actor to the list of actors of this film.
     *
     * @param actor the actor to add.
     */
    public void addActor(Actor actor) {
        if (Objects.isNull(actors)) {
            actors = new ArrayList<>();
        }

        actors.add(actor);
    }

    // ----- FILM CATEGORIES MANY-TO-MANY
    // > film_category(film_id, category_id) FOREIGN KEY(film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "film_category",
            joinColumns = @JoinColumn(name = "film_id", nullable = false, foreignKey = @ForeignKey(
                    name = "film_category_film_id_fkey",
                    foreignKeyDefinition = "FOREIGN KEY (film_id) REFERENCES film(film_id) ON UPDATE CASCADE ON DELETE RESTRICT"
            )),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(
                    name = "film_category_category_id_fkey",
                    foreignKeyDefinition = "FOREIGN KEY (category_id) REFERENCES category(category_id) ON UPDATE CASCADE ON DELETE RESTRICT"
            ))
    )
    private List<Category> categories;

    /**
     * @return the list of categories for this film
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Set the list of categories for this film.
     *
     * @param categories the list of categories.
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Add a category to the list of categories of this film.
     *
     * @param category the category to add.
     */
    public void addCategory(Category category) {
        if (Objects.isNull(categories)) {
            categories = new ArrayList<>();
        }

        categories.add(category);
    }

    // ----- toString
    @Override
    public String toString() {
        return super.toString() + '{'
               + ", title='" + title + '\''
               + ", description='" + description + '\''
               + ", releaseYear='" + releaseYear + '\''
               + ", language=" + language
               + ", originalLanguage=" + originalLanguage
               + ", rentalDuration=" + rentalDuration
               + ", rentalRate=" + rentalRate
               + ", length=" + length
               + ", replacementCost=" + replacementCost
               + ", rating=" + rating
               + ", specialFeatures=" + Arrays.toString(specialFeatures)
               + '}';
    }

}
