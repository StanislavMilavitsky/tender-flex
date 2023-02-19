package pl.exadel.milavitsky.tenderflex.service;

import org.springframework.security.access.prepost.PreAuthorize;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.validation.sort.SortType;

import java.util.List;

/**
 *  Service layer use methods from repository layer
 */

public interface TenderService {

    TenderDto findById(Long id) throws ServiceException;

    TenderDto create(TenderDto tenderDto) throws ServiceException;

    /**
     * Use method findAll in repository layer
     *
     * @return list of all entity
     */
    List<TenderDto> findAllByBidder(int page, int size) throws ServiceException, IncorrectArgumentException;

    /**
     * Use method repository layer that find tender by title or description
     *
     * @param part of word that must be searched
     * @return list of found tenders
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDto> searchByTitleOrDescription(String part) throws ServiceException;

    /**
     * Use method of repository layer and sort by title
     *
     * @param sortType enum value of Class SortType
     * @return sorted list
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDto> sortByTitle(SortType sortType) throws ServiceException;

    /**
     * Use method of repository layer and sort by date start
     *
     * @param sortType enum value of Class SortType
     * @return sorted list
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDto> sortByDateStart(SortType sortType) throws ServiceException;

    /**
     * Use method of repository layer and sort by date end
     *
     * @param sortType enum value of Class SortType
     * @return sorted list
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDto> sortByDateEnd(SortType sortType) throws ServiceException;

    /**
     * Count of all tenders
     *
     * @return count
     * @throws ServiceException if count dont sum
     */
    long count() throws ServiceException;

    /**
     * Use method findAllTenderContractor
     *
     * @param idUser
     * @return list of all tenders of contractor
     */
    List<TenderDto> findAllByContractor(int page, int size, Long idUser) throws ServiceException, IncorrectArgumentException;

    /**
     * Count of all tenders not deleted
     *
     * @param idUser of contractor
     * @return count
     * @throws ServiceException if count dont sum
     */
    long countTendersContractor(Long idUser) throws ServiceException;


    AddTenderDTO collectTenderConstant() throws ServiceException;
}