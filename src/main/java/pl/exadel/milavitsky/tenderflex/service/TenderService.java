package pl.exadel.milavitsky.tenderflex.service;

import org.springframework.security.access.prepost.PreAuthorize;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.CreateTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.FileDto;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.validation.sort.SortType;

import java.io.InputStream;
import java.util.List;

/**
 *  Service layer use methods from repository layer
 */

public interface TenderService {

    @PreAuthorize("hasAuthority('CONTRACTOR') and  hasAuthority('BIDDER')")
    TenderDTO findById(Long id) throws ServiceException;

    @PreAuthorize("hasAuthority('CONTRACTOR')")
    CreateTenderDTO create(CreateTenderDTO createTenderDTO) throws ServiceException;

    TenderDTO update(TenderDTO tenderDTO) throws ServiceException;

    @PreAuthorize("hasAuthority('ADMIN')")
    void deleteById(Long id) throws ServiceException;

    /**
     * Use method findAll in repository layer
     *
     * @return list of all entity
     */
    List<TenderDTO> findAll(int page, int size) throws ServiceException, IncorrectArgumentException;

    /**
     * Use method repository layer that find tender by title or description
     *
     * @param part of word that must be searched
     * @return list of found tenders
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDTO> searchByTitleOrDescription(String part) throws ServiceException;

    /**
     * Use method of repository layer and sort by title
     *
     * @param sortType enum value of Class SortType
     * @return sorted list
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDTO> sortByTitle(SortType sortType) throws ServiceException;

    /**
     * Use method of repository layer and sort by date start
     *
     * @param sortType enum value of Class SortType
     * @return sorted list
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDTO> sortByDateStart(SortType sortType) throws ServiceException;

    /**
     * Use method of repository layer and sort by date end
     *
     * @param sortType enum value of Class SortType
     * @return sorted list
     * @throws ServiceException if the tenders has not been
     */
    List<TenderDTO> sortByDateEnd(SortType sortType) throws ServiceException;

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
     * @param contractorCompany
     * @return list of all tenders of contractor
     */
    List<TenderDTO> findAllByContractor(int page, int size, String contractorCompany) throws ServiceException, IncorrectArgumentException;

    /**
     * Count of all tenders not deleted
     *
     * @param contractorCompany of contractor
     * @return count
     * @throws ServiceException if count dont sum
     */
    long countTendersContractor(String contractorCompany) throws ServiceException;


    AddTenderDTO collectTenderConstant() throws ServiceException;

    FileDto uploadFile(FileDto request);

    InputStream getObject(String filename);
}