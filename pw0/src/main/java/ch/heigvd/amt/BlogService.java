package ch.heigvd.amt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Objects;
import org.hibernate.ObjectNotFoundException;
import org.jboss.logging.annotations.Pos;

@ApplicationScoped
public class BlogService {

    @Inject
    EntityManager em;

    @Transactional
    public Author getOrCreateAuthor(String name) {
        Author author;
        List<Author> list = em.createQuery("SELECT a FROM Author a WHERE a.name = :name", Author.class)
                .setParameter("name", name)
                .getResultList();
        if (list.isEmpty()) {
            author = new Author(name);
            em.persist(author);
        } else {
            author = list.get(0);
        }
        return author;
    }


    @Transactional
    public void createPost(@Valid Post post) {
        em.persist(post);
    }

    @Transactional
    public List<Post> findAllPosts() {
        return em.createQuery("SELECT p FROM Post p", Post.class)
                .getResultList();
    }

    @Transactional
    public Post findPostById(Long id) {
        return em.find(Post.class, id);
    }

    @Transactional
    public Post findPostBySlug(String slug) {
        return em.createQuery("SELECT p FROM Post p WHERE p.slug = :slug", Post.class)
                .setParameter("slug", slug)
                .getSingleResult();
    }

    @Transactional
    public void deletePost(Post post) {
        em.remove(post);
    }

    @Transactional
    public void deletePostBySlug(String slug) {
        var post = findPostBySlug(slug);
        if (Objects.isNull(post)) {
            throw new NotFoundException("post not found");
        }

        deletePost(post);
    }
}
