package pl.exadel.milavitsky.tenderflex.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusOffer;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusTender;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.repository.OfferRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
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


    public static final String FIND_OFFERS_BY_ID_CONTRACTOR_SQL = "SELECT ofs.id, ofs.official_name," +
            " ofs.bid_price, ofs.country, ofs.sent_date, ofs.status, cc.cpv_description " +
            "  FROM offers ofs " +
            " JOIN tenders tn ON ofs.id_tender = tn.id" +
            " JOIN cpv_codes cc ON tn.cpv_code = cc.cpv_code" +
            " WHERE tn.id_user = ? LIMIT ? OFFSET ?";

    private static final String FIND_OFFERS_BY_ID_BIDDER_SQL = "SELECT tn.official_name, cc.cpv_description, ofs.currency, ofs.bid_price," +
            " tn.country, ofs.sent_date, ofs.status, ofs.id" +
            " FROM offers ofs" +
            " JOIN tenders tn ON  ofs.id_tender = tn.id" +
            " JOIN cpv_codes cc ON tn.cpv_code = cc.cpv_code" +
            " WHERE ofs.id_user = ? LIMIT ? OFFSET ?" ;



    private static final String FIND_OFFER_BY_ID_CONTRACTOR_SQL = "SELECT ofs.official_name, ofs.country, ofs.national_registration_number, ofs.city," +
            " ofs.name, ofs.phone_number, ofs.surname, ofs.bid_price, ofs.currency, ofs.document, ofs.status, ofs.sent_date" +
            "  FROM offers ofs" +
            " WHERE ofs.id = ?";

    private static final String FIND_OFFER_BY_ID_BIDDER_SQL = "SELECT ofs.official_name, ofs.country, ofs.national_registration_number, ofs.city," +
            " ofs.name, ofs.phone_number, ofs.surname, ofs.bid_price, ofs.currency, tn.contract, tn.award_decision, tn.reject_decision, cc.cpv_code, cc.cpv_description" +
            " FROM offers ofs" +
            " JOIN tenders tn ON  ofs.id_tender = tn.id" +
            " JOIN cpv_codes cc ON tn.cpv_code = cc.cpv_code" +
            " WHERE ofs.id = ?";

    private static final String UPDATE_STATUS_REJECT_BY_CONTRACTOR_SQL = "UPDATE offers SET status = ?" +
            " WHERE id = ?;";

    private static final String UPDATE_STATUS_APPROVED_BY_CONTRACTOR_SQL = "UPDATE offers SET status = ?" +
            " WHERE id = ?;";

    private static final String UPDATE_STATUS_APPROVED_BY_BIDDER_SQL = "UPDATE offers " +
            " SET status = ? " +
            " WHERE id = ?;";

    private static final String UPDATE_STATUS_CLOSED_BY_BIDDER_SQL = "UPDATE tenders tn SET status = ? FROM offers ofs WHERE tn.id = ofs.id_tender AND ofs.id = ?;";

    private static final String UPDATE_STATUS_DECLINED_BY_BIDDER_SQL = "UPDATE offers SET status = ?" +
            " WHERE id = ?;";

    private static final String COUNT_OF_ALL_OFFERS_CONTRACTOR = "SELECT count(*) FROM offers ofs " +
            "INNER JOIN tenders tn ON ofs.id_tender = tn.id" +
            " WHERE tn.id_user = ?";

    private static final String COUNT_OF_ALL_OFFERS_BIDDER = "SELECT count(*) FROM offers" +
            " WHERE id_user= ?" ;

    @Override
    public List<Offer> findAllOffersByIdContractor(Pageable pageable, Long idUser) throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_OFFERS_BY_ID_CONTRACTOR_SQL, new BeanPropertyRowMapper<>(Offer.class),idUser, pageable.getPageSize(), pageable.getOffset());
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Read offer by id=%d exception sql!", idUser);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public List<OffersTenderBidderDto> findAllOffersByBidder(Pageable pageable, Long idUser) throws RepositoryException {
        try {
        return jdbcTemplate.query(FIND_OFFERS_BY_ID_BIDDER_SQL, new BeanPropertyRowMapper<>(OffersTenderBidderDto.class),idUser, pageable.getPageSize(), pageable.getOffset());
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Read offer by id=%d exception sql!", idUser);
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
            parameters.put(SENT_DATE, offer.getSentDate());
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
    public Offer
    findByIdContractor(Long id) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(FIND_OFFER_BY_ID_CONTRACTOR_SQL, new BeanPropertyRowMapper<>(Offer.class), id);
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Read offer by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public OffersTenderBidderDto findByIdBidder(Long id) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(FIND_OFFER_BY_ID_BIDDER_SQL, new BeanPropertyRowMapper<>(OffersTenderBidderDto.class), id);
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Read offer by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public Offer updateRejectByContractor(Long id) throws RepositoryException {
        try {
            int rows = jdbcTemplate.update(UPDATE_STATUS_REJECT_BY_CONTRACTOR_SQL,StatusOffer.OFFER_REJECTED_BY_CONTRACTOR.name() , id);
            return  rows > 0L ? findByIdContractor(id) : null;
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Update tender by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public Offer updateApprovedByContractor(Long id) throws RepositoryException {
        try {
            int rows = jdbcTemplate.update(UPDATE_STATUS_APPROVED_BY_CONTRACTOR_SQL,StatusOffer.OFFER_APPROVED_BY_CONTRACTOR.name(), id);
            return  rows > 0L ? findByIdContractor(id) : null;
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Update tender by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public Offer updateApprovedByBidder(Long id) throws RepositoryException {
        try {
            int rows = jdbcTemplate.update(UPDATE_STATUS_APPROVED_BY_BIDDER_SQL,StatusOffer.CONTRACT_APPROVED_BY_BIDDER.name(), id);
            int rows2 = jdbcTemplate.update(UPDATE_STATUS_CLOSED_BY_BIDDER_SQL, StatusTender.CLOSED.name(), id);
            return  rows + rows2 > 1L ? findByIdContractor(id) : null;
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Update tender by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public Offer updateDeclinedByBidder(Long id) throws RepositoryException {
        try {
            int rows = jdbcTemplate.update(UPDATE_STATUS_DECLINED_BY_BIDDER_SQL, StatusOffer.CONTRACT_DECLINED_BY_BIDDER.name(), id);
            return  rows > 0L ? findByIdContractor(id) : null;
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Update tender by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public long countOfOffersByContractor(Long idUser) {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_OFFERS_CONTRACTOR, Long.class, idUser);
    }

    @Override
    public long countOfOffersByBidder(Long idUser) {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_OFFERS_BIDDER, Long.class, idUser);
    }


}
