package ch.heigvd.amt.jpa.service;

import ch.heigvd.amt.jpa.entity.Staff;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
@TestTransaction
class StaffServiceTest {

    @Inject
    StaffService staffService;

    @Test
    void findStaffByUsername_ExistingStaff_ReturnsStaff() {
        Staff found = staffService.findStaffByUsername("Mike");

        assertNotNull(found);
        assertEquals("Mike", found.getUsername());
        assertEquals("Mike", found.getFirstName());
        assertEquals("Hillyer", found.getLastName());
    }

    @Test
    void findStaffByUsername_NonexistentStaff_ThrowsException() {
        assertThrows(NoResultException.class, () -> staffService.findStaffByUsername("nonexistent"));
    }
}
