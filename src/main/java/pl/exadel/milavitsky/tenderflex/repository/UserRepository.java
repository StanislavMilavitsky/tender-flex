package pl.exadel.milavitsky.tenderflex.repository;

import pl.exadel.milavitsky.tenderflex.entity.User;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Work with user in layer repository
 */
public interface UserRepository {

    /**
     * Find all users
     * @param pageable size and page of view
     * @return
     */
    List<User> findAll(Pageable pageable);

    /**
     * Count of entity
     * @return count
     */
    long countOfEntity();

    /**
     * Get user by username
     * @param username is username
     * @return user from db
     */
    Optional<User> findByUsername(String username);
}