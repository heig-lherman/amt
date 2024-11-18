package ch.heigvd.amt.jpa.service;

import ch.heigvd.amt.jpa.entity.Staff;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import java.util.Objects;

/**
 * Service to manage fetch and manage staff members.
 */
@ApplicationScoped
public class StaffService {

    private final EntityManager em;

    public StaffService(EntityManager em) {
        this.em = em;
    }

    /**
     * Find a staff member by its username.
     *
     * @param username the username of the staff member to find
     * @return the staff member
     */
    public Staff findStaffByUsername(String username) {
        return Objects.requireNonNull(
            em.createQuery("SELECT s FROM Staff s WHERE s.username = :username", Staff.class)
                .setParameter("username", username)
                .getSingleResult()
        );
    }
}
