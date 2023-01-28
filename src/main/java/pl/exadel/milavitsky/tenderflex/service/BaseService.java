package pl.exadel.milavitsky.tenderflex.service;

import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

/**
 *  Service layer use methods from repository layer
 */
public interface BaseService<T> {

    /**
     * Use method findById in repository layer
     * @param id is field t
     * @return entity from database
     * @throws ServiceException if entity by id has not been exist
     */
    T findById(Long id) throws ServiceException;

    /**
     * Use method create in repository layer
     * @param t entity
     * @return created entity
     * @throws ServiceException if the entity has not been added to the database
     */
    T create (T t) throws ServiceException;


    /**
     * Use method update in repository layer
     * @param t entity
     * @return updated entity
     * @throws ServiceException if the entity has not been updated to the database
     */
    T update (T t) throws ServiceException;


    /**
     * Use method delete in repository layer
     * @param id is field entity
     * @throws ServiceException  if the entity has not been deleted to the database
     */

    void deleteById (Long id) throws ServiceException;


}