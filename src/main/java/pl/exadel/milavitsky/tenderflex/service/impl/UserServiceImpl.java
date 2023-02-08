package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.exadel.milavitsky.tenderflex.entity.Role;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.repository.UserRepository;
import pl.exadel.milavitsky.tenderflex.service.Page;
import pl.exadel.milavitsky.tenderflex.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) throws ServiceException {
        try {
            return userRepository.findById(id);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find user by id=%d !", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public User create(User user) throws ServiceException {
        try {
            if (user.getRole() == null ){
                user.setRole(Role.BIDDER);
            }

            Optional.ofNullable(user.getPassword())
                    .filter(StringUtils::hasText)
                    .map(passwordEncoder::encode)
                    .ifPresent(user::setPassword);

            return userRepository.create(user);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Add user by username=%s exception!", user.getUserName());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public User update(User user) throws ServiceException {
        try {
            return userRepository.update(user);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update user by  username=%s exception!", user.getUserName());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            userRepository.delete(id);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Delete user by id=%d exception!", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    // @PostFilter("filterObject.role.name().equals('ADMIN')")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> findAll(int page, int size) throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page userPage = new Page(page, size, count);
            return new ArrayList<>(userRepository.findAll(userPage.getOffset(), userPage.getLimit()));
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find all users service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public long count() throws ServiceException {
        try {
            return userRepository.countOfEntity();
        } catch (DataAccessException exception) {
            String exceptionMessage = "Count users service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUserName(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user:" + username));
    }
}