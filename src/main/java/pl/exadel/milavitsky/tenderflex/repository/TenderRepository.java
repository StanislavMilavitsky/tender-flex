package pl.exadel.milavitsky.tenderflex.repository;

import org.springframework.data.domain.Pageable;
import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;

import java.util.List;

/**
 * Work with tender in layer repository
 */
public interface TenderRepository {

    List<Tender> findAllByContractor(Pageable pageable, Long id) throws RepositoryException;

    /**
     * Get count of Tenders at database
     *
     * @param idUser id
     * @return count
     */
    long countOfTendersContractor(Long idUser);

    /**
     * Create an entity in database with fields
     * @return entity
     * @throws RepositoryException if have not been created
     */
    Tender create (Tender tender) throws RepositoryException;

    /**
     * Read an entity from database by id
     *
     * @param id field from entity
     * @return entity tag
     *@throws RepositoryException if entity have not been found
     */
    Tender findById(Long id) throws RepositoryException;

    /**
     * Find all tender by bidder
     *
     * @param pageable size and page of view
     * @param idUser
     * @return
     * @throws RepositoryException
     */
    List<Tender> findAllByBidder(Pageable pageable, Long idUser)throws RepositoryException;

    /**
     * Count of tenders
     *
     * @return count
     */
    long countOfEntity();

    /**
     * Find all cpv codes and description in data base
     *
     * @return list entity CPVCode
     */
    List<CPVCode> findAllCPVCodes();

}