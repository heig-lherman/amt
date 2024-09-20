package ch.heigvd.amt;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"slug"}))
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String slug;

    @NotNull
    @Column(nullable = false)
    private Instant date;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Post() {
    }

    public Post(String slug, Instant date, String title, String content, Author author) {
        this.id = id;
        this.slug = slug;
        this.date = date;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
