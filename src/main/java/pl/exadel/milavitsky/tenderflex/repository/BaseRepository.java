package pl.exadel.milavitsky.tenderflex.repository;

import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;

import java.util.List;

public interface BaseRepository <T> {

    /**
     * Create an entity in database with fields
     * @return entity
     * @throws RepositoryException if have not been created
     */
    T create (T t) throws RepositoryException;

    /**
     * Read an entity from database by id
     * @param id field from entity
     * @return entity tag
     *@throws RepositoryException if entity have not been found
     */
    T findById(Long id) throws RepositoryException;

    /**
     * Find all in database
     * @param offset the offset
     * @param limit the limit
     * @return list of entity
     */
    List<T> findAllByBidder(int offset, int limit);

    /**
     * Get count of all users from db
     * @return count of users
     */
    long countOfEntity();

}