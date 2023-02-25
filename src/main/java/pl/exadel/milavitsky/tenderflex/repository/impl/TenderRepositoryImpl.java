package pl.exadel.milavitsky.tenderflex.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.repository.TenderRepository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.exadel.milavitsky.tenderflex.repository.constant.ConstantRepository.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TenderRepositoryImpl implements TenderRepository {

    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;
    @PostConstruct
    private void postConstruct() {
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("tenders")
                .usingGeneratedKeyColumns(ID);
    }


    public static final String FIND_TENDER_BY_ID_SQL = "SELECT tn.official_name, tn.country, tn.national_registration_number,tn.city, tn.name, tn.phone_number," +
            "       tn.surname, tn.cpv_code, tn.status, tn.minimum_tender_value, tn.maximum_tender_value, tn.currency," +
            "       tn.description_of_the_procurement, tn.publication_date, tn.deadline_for_offer_submission," +
            "       tn.deadline_for_signing_contract_submission, tn.contract, tn.award_decision, tn.reject_decision," +
            "       cc.cpv_description" +
            "    FROM tenders tn" +
            "    JOIN cpv_codes cc ON tn.cpv_code = cc.cpv_code WHERE tn.id_user =? LIMIT ? OFFSET ?";


    private static final String COUNT_OF_ALL_TENDERS_SQL = "SELECT count(*) FROM tenders;";

    private static final String COUNT_OF_ALL_TENDERS_CONTRACTOR_SQL = "SELECT count(*) FROM tenders WHERE id_user = ?;";

    private static final String FIND_ALL_TENDERS_CONTRACTOR_SQL = "SELECT tn.cpv_code, cc.cpv_description, tn.official_name, tn.status, tn.deadline_for_signing_contract_submission, COUNT(ofc.id) AS \"count_of_offers\" " +
            " FROM tenders tn" +
            " JOIN cpv_codes cc ON tn.cpv_code = cc.cpv_code" +
            " JOIN offers ofs tn.id = ofs.id_tender" +
            " WHERE id_user = ? "+
            " GROUP BY tn.id " +
            " HAVING ofc.id_tender = tn.id" +
            " LIMIT ? OFFSET ?";


    private static final String FIND_ALL_CPV_CODES_SQL = "SELECT cpv_code, cpv_description FROM cpv_codes; ";

    @Override
    public Tender create(Tender tender) throws RepositoryException {
        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put(OFFICIAL_NAME, tender.getOfficialName());
            parameters.put(NATIONAL_REGISTRATION_NUMBER, tender.getNationalRegistrationNumber());
            parameters.put(COUNTRY, tender.getCountry().name());
            parameters.put(CITY, tender.getCity());
            parameters.put(NAME, tender.getName());
            parameters.put(SURNAME, tender.getSurname());
            parameters.put(PHONE_NUMBER, tender.getPhoneNumber());
            parameters.put(TYPE_OF_TENDER, tender.getTypeOfTender().name());
            parameters.put(DESCRIPTION_OF_THE_PROCUREMENT, tender.getDescriptionOfTheProcurement());
            parameters.put(MINIMUM_TENDER_VALUE, tender.getMinimumTenderValue());
            parameters.put(MAXIMUM_TENDER_VALUE, tender.getMaximumTenderValue());
            parameters.put(CURRENCY, tender.getCurrency().name());
            parameters.put(PUBLICATION_DATE, tender.getPublicationDate());
            parameters.put(DEADLINE_FOR_OFFER_SUBMISSION, tender.getDeadlineForOfferSubmission());
            parameters.put(DEADLINE_FOR_SIGNING_CONTRACT_SUBMISSION, tender.getDeadlineForSigningContractSubmission());
            parameters.put(CONTRACT, tender.getContract());
            parameters.put(AWARD_DECISION, tender.getAwardDecision());
            parameters.put(REJECT_DECISION, tender.getRejectDecision());
            parameters.put(STATUS, tender.getStatusTender().name());
            parameters.put(ID_USER, tender.getIdUser());

            Number id = jdbcInsert.executeAndReturnKey(parameters);
            tender.setId(id.longValue());
            return tender;
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Create tender by company name=%s exception sql!", tender.getOfficialName());
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public List<Tender> findAllById(Pageable pageable, Long id) throws RepositoryException{
        try {
            return jdbcTemplate.query(FIND_TENDER_BY_ID_SQL, new BeanPropertyRowMapper<>(Tender.class),
                    id, pageable.getPageSize(), pageable.getOffset());
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Read tender by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public Tender findById(Long id) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(FIND_TENDER_BY_ID_SQL, new BeanPropertyRowMapper<>(Tender.class), id);
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Read tender by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public long countOfEntity() {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_TENDERS_SQL, Long.class);
    }

    @Override
    public long countOfTendersContractor(Long idUser) {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_TENDERS_CONTRACTOR_SQL, Long.class, idUser);
    }

    @Override
    public List<CPVCode> findAllCPVCodes() {
        return jdbcTemplate.query(FIND_ALL_CPV_CODES_SQL, new BeanPropertyRowMapper<>(CPVCode.class));

    }

}
