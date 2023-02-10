package pl.exadel.milavitsky.tenderflex.repository;

import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;

import java.util.List;
import java.util.Optional;

/**
 * Work with user in layer repository
 */
public interface UserRepository {

    User create(User user) throws RepositoryException;

    User findById(Long id) throws RepositoryException;

    List<User> findAll(int offset, int limit);

    long countOfEntity();

    /**
     * Get user by username
     * @param username is username
     * @return user from db
     */
    Optional<User> findByUsername(String username);
}