package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;

import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.repository.UserRepository;
import pl.exadel.milavitsky.tenderflex.service.UserService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;


    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> findAll(Pageable pageable) throws ServiceException {
        try {
            List<User> users = userRepository.findAll(pageable);
            long cont = userRepository.countOfEntity();
            Page<User> pages = new PageImpl<>(users, pageable, cont);
            return pages;
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find all users service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user:" + username));
    }

}