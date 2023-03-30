package pl.exadel.milavitsky.tenderflex.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.entity.enums.Role;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {
    @Mock
    private UserRepository repository;

    private UserServiceImpl service;
    private Pageable pageable;
    private User user;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository);
        pageable = PageRequest.of(1, 1);
        user = User.builder().id(1L).role(Role.ADMIN).username("User").password("123").build();
    }

    @Test
    void testFindAllPositive() throws ServiceException {
        List<User> users = new ArrayList<>();
        users.add(user);
        PageImpl<User> expected = new PageImpl<>(users, pageable, 2);
        when(repository.findAll(pageable)).thenReturn(users);
        when(repository.countOfEntity()).thenReturn(2L);
        Page<User> actual = service.findAll(pageable);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllNegative() throws ServiceException {
        List<User> users = new ArrayList<>();
        users.add(user);
        PageImpl<User> expected = new PageImpl<>(users, pageable, 2);
        when(repository.findAll(pageable)).thenReturn(users);
        when(repository.countOfEntity()).thenReturn(3L);
        Page<User> actual = service.findAll(pageable);
        assertNotEquals(expected, actual);
    }

    @Test
    void testFindAllException() {
        when(repository.findAll(pageable)).thenThrow(new DataAccessException("...") {});
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findAll(pageable), "Find all users service exception!");
        assertEquals(exception.getMessage(), "Find all users service exception!");
    }

    @Test
    void testLoadUserByUsernamePositive() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserDetails actual = service.loadUserByUsername(user.getUsername());
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("User")
                .password("123")
                .roles("ADMIN").build();
        assertEquals(userDetails, actual);
    }

    @Test
    void testLoadUserByUsernameNegative() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserDetails actual = service.loadUserByUsername(user.getUsername());
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("User1")
                .password("123")
                .roles("ADMIN").build();
        assertNotEquals(userDetails, actual);
    }
}