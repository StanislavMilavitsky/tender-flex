package pl.exadel.milavitsky.tenderflex.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.repository.OfferRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.exadel.milavitsky.tenderflex.repository.constant.ConstantRepository.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OfferRepositoryImpl implements OfferRepository {

    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    @PostConstruct
    private void postConstruct() {
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("offers")
                .usingGeneratedKeyColumns(ID);
    }


    public static final String FIND_OFFERS_BY_ID_TENDER_SQL = "SELECT of.id, of.company_bidder, of.offer," +
            " of.offer_description, of.id_tender, of.answer" +
            "  FROM offers of " +
            "INNER JOIN tenders tn ON of.id_tender = tn.id" +
            " WHERE of.id_tender = ? and tn.is_deleted = false;";

    private static final String COUNT_OF_ALL_OFFERS = "SELECT count(*) FROM offers" +
            "INNER JOIN tenders tn ON of.id_tender = tn.id" +
            " WHERE id_tender = ? and tn.is_deleted = false;";

    @Override
    public List<Offer> findAllOffersByIdTender(int offset, int limit, Long id) throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_OFFERS_BY_ID_TENDER_SQL, new BeanPropertyRowMapper<>(Offer.class), id);
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Read offer by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public Offer create(Offer offer) throws RepositoryException {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(ID_TENDER, offer.getIdTender());
            parameters.put(OFFICIAL_NAME, offer.getOfficialName());
            parameters.put(NATIONAL_REGISTRATION_NUMBER, offer.getNationalRegistrationNumber());
            parameters.put(COUNTRY, offer.getCountry().name());
            parameters.put(CITY, offer.getCity());
            parameters.put(NAME, offer.getName());
            parameters.put(SURNAME, offer.getSurname());
            parameters.put(PHONE_NUMBER, offer.getPhoneNumber());
            parameters.put(BID_PRICE, offer.getBidPrice());
            parameters.put(CURRENCY, offer.getCurrency().name());
            parameters.put(DOCUMENT, offer.getDocument());
            parameters.put(STATUS, offer.getStatus().name());
            parameters.put(SEND_DATE, offer.getSendDate());
            parameters.put(ID_USER, offer.getIdUser());

            Number id = jdbcInsert.executeAndReturnKey(parameters);
            offer.setId(id.longValue());
            return offer;
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Create offer by tender id=%d exception sql!", offer.getIdTender());
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public long countOfEntity() {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_OFFERS, Long.class);
    }
}
