package pl.exadel.milavitsky.tenderflex.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pl.exadel.milavitsky.tenderflex.entity.User;
import pl.exadel.milavitsky.tenderflex.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.util.List;
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

    public static final String FIND_ALL_USERS_SQL = "SELECT us.id," +
            " username," +
            " role," +
            " last_login_date" +
            " FROM users us LIMIT ? OFFSET ?";

    private static final String COUNT_OF_ALL_USERS_SQL = "SELECT count(*) FROM users WHERE is_deleted = false;";//todo

    private static final String FIND_USER_BY_USERNAME_SQL = "SELECT us.id, username," +
            " password, date_of_registration, role, last_login_date " +
            "FROM users us WHERE us.username = ?;";



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