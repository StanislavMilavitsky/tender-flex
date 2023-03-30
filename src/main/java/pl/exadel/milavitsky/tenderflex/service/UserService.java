package pl.exadel.milavitsky.tenderflex.service;

import org.springframework.data.domain.Page;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

import org.springframework.data.domain.Pageable;

public interface UserService {

    /**
     * Find all users
     *
     * @param pageable page and size of view
     * @return list of users
     * @throws ServiceException service exception
     */
    Page<User> findAll(Pageable pageable) throws ServiceException;

}