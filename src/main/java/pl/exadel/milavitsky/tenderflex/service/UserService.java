package pl.exadel.milavitsky.tenderflex.service;

import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

import java.util.List;

public interface UserService {

    User findById(Long id) throws ServiceException;

    User create(User user) throws ServiceException;

    List<User> findAll(int page, int size) throws ServiceException, IncorrectArgumentException;

    long count() throws ServiceException;
}