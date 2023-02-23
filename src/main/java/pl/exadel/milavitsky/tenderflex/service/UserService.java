package pl.exadel.milavitsky.tenderflex.service;

import org.springframework.data.domain.Page;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> findAll(Pageable pageable) throws ServiceException, IncorrectArgumentException;

}