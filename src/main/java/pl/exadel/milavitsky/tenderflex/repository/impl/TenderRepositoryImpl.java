package pl.exadel.milavitsky.tenderflex.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.repository.TenderRepository;
import pl.exadel.milavitsky.tenderflex.validation.sort.SortType;

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


    public static final String FIND_TENDER_BY_ID_SQL = "SELECT tn.id, tn.title, tn.tender_description, tn.budget, tn.date_of_start, tn.is_deleted, tn.date_of_end, tn.user_company" +
            "  FROM tenders tn WHERE tn.id = ?;";

    public static final String UPDATE_TENDER_BY_ID_SQL = "UPDATE tenders SET title = ?," +
            "tender_description = ?," +
            "budget = ?," +
            "date_of_start = ?," +
            "date_of_end = ? " +
            "WHERE id = ?;";

    public static final String DELETE_TENDER_BY_ID_SQL = "UPDATE tenders SET is_deleted = true WHERE tenders.id = ?;";

    public static final String FIND_ALL_TENDERS_SQL = "SELECT tn.id, tn.title, tn.Tender_description, tn.budget, tn.date_of_start, tn.date_of_end, tn.is_deleted, tn.user_company" +
            " FROM tenders tn" +
            " LIMIT ? OFFSET ?;";

    private static final String SELECT_BY_TITLE_OR_DESCRIPTION_SQL = "SELECT tn.id, tn.title, tn.Tender_description, tn.budget, tn.date_of_start," +
            " tn.date_of_end, tn.is_deleted, tn.user_company FROM tenders tn WHERE tn.title LIKE ? OR pr.tender_description LIKE ?";

    private static final String SORT_BY_TITLE_SQL = "SELECT id, title, tender_description, budget, date_of_start, date_of_end, is_deleted, tn.user_company" +
            " FROM tenders ORDER BY title ";

    private static final String SORT_BY_DATE_SQL_START_SQL = "SELECT tn.id, title, tender_description, budget, date_of_start, date_of_end, is_deleted, tn.user_company " +
            "FROM tenders tn ORDER BY tn.date_of_start ";

    private static final String SORT_BY_DATE_SQL_END = "SELECT tn.id, title, tender_description, budget, date_of_start, date_of_end, is_deleted, tn.user_company " +
            "FROM tenders pr ORDER BY tn.date_of_end ";

    private static final String COUNT_OF_ALL_TENDERS = "SELECT count(*) FROM tenders;";

    private static final String COUNT_OF_ALL_TENDERS_CONTRACTOR = "SELECT count(*) FROM tenders WHERE id_user = ? AND is_deleted = FALSE;";

    private static final String FIND_ALL_TENDERS_CONTRACTOR = "SELECT tn.cpv_code, cc.cpv_description, tn.official_name, tn.status, tn.deadline_for_signing_contract_submission, COUNT(ofc.id) AS \"offers\" " +
            " FROM tenders tn" +
            " JOIN cpv_codes cc ON tn.cpv_code = cc.cpv_code" +
            " JOIN offers ofs tn.id = ofs.id_tender" +
            " GROUP BY tn.id " +
            " HAVING ofc.id_tender = tn.id" +
            " WHERE id_user = ? LIMIT ? OFFSET ?;";

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
            parameters.put(CPV_CODE, tender.getCpvCode().getCpvCode());
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
    public Tender update(Tender tender) throws RepositoryException {
        try {
           /* int rows = jdbcTemplate.update(UPDATE_TENDER_BY_ID_SQL, tender.getTitle(),
                    tender.getTenderDescription(), tender.getBudget(),
                    tender.getDateOfStart(), tender.getDateOfEnd(), tender.getId());*/
            return null;// rows > 0L ? findById(tender.getId()) : null;
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Update tender by id=%d exception sql!", tender.getId());
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        try {
            int rows = jdbcTemplate.update(DELETE_TENDER_BY_ID_SQL, id);
            if (rows < 0) {
                String message = String.format("Entity by id=%d was deleted!", id);
                log.error(message);
                throw new RepositoryException(message);
            }
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Delete tender by id=%d exception sql!", id);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public List<Tender> findAll(int offset, int limit) {
        return jdbcTemplate.query(FIND_ALL_TENDERS_SQL, new BeanPropertyRowMapper<>(Tender.class), limit, offset);
    }

    @Override
    public List<Tender> searchByTitleOrDescription(String part) throws RepositoryException {
        try {
            String sqlPart = PERCENT + part + PERCENT;
            return jdbcTemplate.query(SELECT_BY_TITLE_OR_DESCRIPTION_SQL, new BeanPropertyRowMapper<>(Tender.class), sqlPart, sqlPart);
        } catch (DataAccessException exception) {
            String exceptionMessage = String.format("Find tender by word=%s exception sql!", part);
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public List<Tender> sortByTitle(SortType sortType) throws RepositoryException {
        try {
            StringBuilder builder = new StringBuilder(SORT_BY_TITLE_SQL);
            if (sortType == SortType.DESC) {
                builder.append(SortType.DESC.name());
            }
            return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper<>(Tender.class));
        } catch (DataAccessException exception) {
            String exceptionMessage = "Sort tenders by title exception sql";
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public List<Tender> sortByDateOfStart(SortType sortType) throws RepositoryException {
        try {
            StringBuilder builder = new StringBuilder(SORT_BY_DATE_SQL_START_SQL);
            if (sortType == SortType.DESC) {
                builder.append(SortType.DESC.name());
            }
            return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper<>(Tender.class));
        } catch (DataAccessException exception) {
            String exceptionMessage = "Sort tender by date of start exception sql";
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public List<Tender> sortByDateOfEnd(SortType sortType) throws RepositoryException {
        try {
            StringBuilder builder = new StringBuilder(SORT_BY_DATE_SQL_END);
            if (sortType == SortType.DESC) {
                builder.append(SortType.DESC.name());
            }
            return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper<>(Tender.class));
        } catch (DataAccessException exception) {
            String exceptionMessage = "Sort tender by date of end exception sql";
            log.error(exceptionMessage, exception);
            throw new RepositoryException(exceptionMessage, exception);
        }
    }

    @Override
    public long countOfEntity() {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_TENDERS, Long.class);
    }

    @Override
    public long countOfTendersContractor(Long id_user) {
        return jdbcTemplate.queryForObject(COUNT_OF_ALL_TENDERS_CONTRACTOR, Long.class, id_user);
    }

    @Override
    public List<Tender> findAllTendersContractor(int offset, int limit, Long id_user) {
        return jdbcTemplate.query(FIND_ALL_TENDERS_CONTRACTOR, new BeanPropertyRowMapper<>(Tender.class),id_user, limit, offset);
    }

    @Override
    public List<CPVCode> findAllCPVCodes() {
        return jdbcTemplate.query(FIND_ALL_CPV_CODES_SQL, new BeanPropertyRowMapper<>(CPVCode.class));

    }

}
