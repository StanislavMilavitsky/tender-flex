package pl.exadel.milavitsky.tenderflex.repository;

import pl.exadel.milavitsky.tenderflex.entity.User;

import java.util.Optional;

/**
 * Work with user in layer repository
 */
public interface UserRepository extends BaseRepository<User> {

    /**
     * Get user by username
     * @param username is username
     * @return user from db
     */
    Optional<User> findByUsername(String username);
}