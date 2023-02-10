package pl.exadel.milavitsky.tenderflex.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.exadel.milavitsky.tenderflex.repository.constant.ConstantRepository.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    private void postConstruct(){
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns(ID);
    }

    private static final String FIND_USER_BY_ID_SQL = "SELECT us.id," +
            " username," +
            " password," +
            " date_of_registration," +
            " role, company, is_deleted" +
            " FROM users us" +
            " WHERE us.id = ?;";

    public static final String FIND_ALL_USERS_SQL = "SELECT us.id," +
            " username," +
            " password," +
            " date_of_registration," +
            " role, company, is_deleted FROM users us LIMIT ? OFFSET ?";

    private static final String COUNT_OF_ALL_USERS_SQL = "SELECT count(*) FROM users WHERE is_deleted = false;";//todo

    private static final String FIND_USER_BY_USERNAME_SQL = "SELECT us.id, username," +
            " password, date_of_registration, role,company ,is_deleted " +
            "FROM users us WHERE us.username = ?;";

    @Override
    public User create(User user) throws RepositoryException {
        try{
            user.setDateOfRegistration(LocalDate.now());
            user.setIsDeleted(false);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(USERNAME, user.getUserName());
            parameters.put(PASSWORD, user.getPassword());
            parameters.put(DATE_OF_REGISTRATION, user.getDateOfRegistration() );
            parameters.put(ROLE, user.getRole().toString());
            parameters.put(COMPANY, user.getCompany().toString());
            parameters.put(IS_DELETED, user.getIsDeleted());
            Number id = jdbcInsert.executeAndReturnKey(parameters);
            user.setId(id.longValue());
            return user;
        } catch (DataAccessException exception){
            String exceptionMessage = String.format("Create user by Last username=%s exception sql!", user.getUserName());
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public User findById(Long id) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID_SQL, new BeanPropertyRowMapper<>(User.class), id);
        } catch (DataAccessException exception){
            String exceptionMessage = String.format("Read user by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public List<User> findAll(int offset, int limit) {
        return jdbcTemplate.query(FIND_ALL_USERS_SQL,new BeanPropertyRowMapper<>(User.class), limit, offset);
    }


    @Override
    public long countOfEntity() {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_USERS_SQL, Long.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME_SQL, new BeanPropertyRowMapper<>(User.class), username));
    }

}