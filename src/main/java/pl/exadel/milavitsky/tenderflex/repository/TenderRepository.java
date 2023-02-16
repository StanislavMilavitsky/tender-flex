package pl.exadel.milavitsky.tenderflex.repository;

import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.validation.sort.SortType;

import java.util.List;

/**
 * Work with tender in layer repository
 */
public interface TenderRepository extends BaseRepository <Tender> {

    /**
     * Search Tenders in database by title or description
     * @param part it is String value of word
     * @return list of Tenders
     * @throws RepositoryException if Tenders have not been found
     */
    List<Tender> searchByTitleOrDescription(String part) throws RepositoryException;

    /**
     * Select entity from database and sort them by title
     * @param sortType is type from enum class SortType
     * @return list of sorted entity
     * @throws RepositoryException if Tenders have not been sorted
     */
    List<Tender> sortByTitle(SortType sortType) throws RepositoryException;

    /**
     * Select entity from database and sort them by date of start
     * @param sortType is type from enum class SortType
     * @return list of sorted entity
     * @throws RepositoryException if Tenders have not been sorted
     */
    List<Tender> sortByDateOfStart(SortType sortType) throws RepositoryException;

    /**
     * Select entity from database and sort them by date of end
     * @param sortType is type from enum class SortType
     * @return list of sorted entity
     * @throws RepositoryException if Tenders have not been sorted
     */
    List<Tender> sortByDateOfEnd(SortType sortType) throws RepositoryException;

    /**
     * Get count of Tenders at database not deleted
     *
     * @param id_user
     * @return count
     */
    long countOfTendersContractor(Long id_user);

    /**
     * Find all Tenders not deleted
     *
     * @param id_user
     * @param offset offset
     * @param limit limit
     * @return list of Tenders
     */
    List<Tender> findAllTendersContractor(int offset, int limit, Long id_user);

    List<CPVCode> findAllCPVCodes();

}