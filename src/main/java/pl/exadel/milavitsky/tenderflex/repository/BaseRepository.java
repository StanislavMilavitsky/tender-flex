package pl.exadel.milavitsky.tenderflex.repository;

import org.springframework.data.domain.Pageable;
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
     *
     * @return list of entity
     */
    List<T> findAllById(Pageable pageable, Long id)throws RepositoryException;

    /**
     * Get count of all users from db
     * @return count of users
     */
    long countOfEntity();

}