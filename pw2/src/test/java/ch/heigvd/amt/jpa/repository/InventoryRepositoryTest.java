package ch.heigvd.amt.jpa.repository;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
@TestTransaction
class InventoryRepositoryTest {
    @Inject
    InventoryRepository inventoryRepository;
    @Inject
    EntityManager em;

    @Test
    public void testRead() {
        var i = inventoryRepository.read(854);
        assertEquals(188, i.filmId());
        assertEquals(1, i.storeId());
    }

    @Test
    public void testCreate() {
        var i = InventoryRepository.InventoryDTO.create(1, 1);
        var inventory = inventoryRepository.create(i);
        assertNotNull(inventory.id());
        assertEquals(i.filmId(), inventory.filmId());
        assertEquals(i.storeId(), inventory.storeId());

        flushAndClear();

        var iread = inventoryRepository.read(inventory.id());
        assertEquals(inventory.id(), iread.id());
        assertEquals(i.filmId(), iread.filmId());
        assertEquals(i.storeId(), iread.storeId());
    }

    @Test
    public void testUpdate() {
        var u = new InventoryRepository.InventoryDTO(854, 188, 2);
        inventoryRepository.update(u);

        flushAndClear();

        var e = inventoryRepository.read(u.id());
        assertEquals(u, e);
    }

    @Test
    public void testDelete() {
        var i = InventoryRepository.InventoryDTO.create(1, 1);
        var e = inventoryRepository.create(i);
        assertNotNull(e.id());
        assertEquals(i.filmId(), e.filmId());
        assertEquals(i.storeId(), e.storeId());

        flushAndClear();

        inventoryRepository.delete(e.id());

        flushAndClear();

        var er = inventoryRepository.read(e.id());
        assertNull(er);
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
