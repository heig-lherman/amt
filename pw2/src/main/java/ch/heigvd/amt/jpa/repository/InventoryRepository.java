package ch.heigvd.amt.jpa.repository;

import ch.heigvd.amt.jpa.entity.Film;
import ch.heigvd.amt.jpa.entity.Inventory;
import ch.heigvd.amt.jpa.entity.Store;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * Repository for the {@link Inventory} entity.
 */
@ApplicationScoped
public class InventoryRepository {
    @Inject
    private EntityManager em;

    public record InventoryDTO(Integer id, Integer filmId, Integer storeId) {
        public static InventoryDTO create(Integer filmId, Integer storeId) {
            return new InventoryDTO(null, filmId, storeId);
        }
    }

    public InventoryDTO read(Integer id) {
        Inventory inventory = em.find(Inventory.class, id);
        if (inventory == null) {
            return null;
        }
        return new InventoryDTO(
                inventory.getId(),
                inventory.getFilm().getId(),
                inventory.getStore().getId()
        );
    }

    @Transactional
    public InventoryDTO create(InventoryDTO inventoryDTO) {
        Film film = em.find(Film.class, inventoryDTO.filmId());
        Store store = em.find(Store.class, inventoryDTO.storeId());

        if (film == null || store == null) {
            throw new IllegalArgumentException("Film or Store not found");
        }

        Inventory inventory = new Inventory();
        inventory.setFilm(film);
        inventory.setStore(store);

        em.persist(inventory);
        em.flush();

        return new InventoryDTO(
                inventory.getId(),
                inventory.getFilm().getId(),
                inventory.getStore().getId()
        );
    }

    @Transactional
    public void update(InventoryDTO inventoryDTO) {
        Inventory inventory = em.find(Inventory.class, inventoryDTO.id());
        if (inventory != null) {
            Film film = em.find(Film.class, inventoryDTO.filmId());
            Store store = em.find(Store.class, inventoryDTO.storeId());

            if (film == null || store == null) {
                throw new IllegalArgumentException("Film or Store not found");
            }

            inventory.setFilm(film);
            inventory.setStore(store);
            em.merge(inventory);
        }
    }

    @Transactional
    public void delete(Integer id) {
        Inventory inventory = em.find(Inventory.class, id);
        if (inventory != null) {
            em.remove(inventory);
        }
    }
}
