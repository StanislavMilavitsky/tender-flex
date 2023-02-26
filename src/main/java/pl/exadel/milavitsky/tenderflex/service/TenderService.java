package pl.exadel.milavitsky.tenderflex.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;


/**
 *  Service layer use methods from repository layer
 */

public interface TenderService {

    /**
     * Find tender by id
     *
     * @param id tender
     * @return entity tender
     * @throws ServiceException
     */
    TenderDto findById(Long id) throws ServiceException;

    /**
     * Create tender
     *
     * @param tenderDto dto of tender
     * @return created entity
     * @throws ServiceException service exception
     */
    TenderDto create(TenderDto tenderDto) throws ServiceException;

    /**
     * Find all tenders by bidder
     *
     * @param pageable page and size of view
     * @param idUser
     * @return list of tenders dto
     * @throws ServiceException
     * @throws IncorrectArgumentException
     */
    Page<TenderDto> findAllByBidder(Pageable pageable, Long idUser) throws ServiceException, IncorrectArgumentException;


    /**
     * Find all tenders by contractor
     *
     * @param pageable page and size of view
     * @param idUser
     * @return list of tenders dto
     * @throws ServiceException
     * @throws IncorrectArgumentException
     */
    Page<TenderDto> findAllByContractor(Pageable pageable, Long idUser) throws ServiceException, IncorrectArgumentException;

    /**
     * Count of all tenders
     *
     * @param idUser of contractor
     * @return count
     * @throws ServiceException if count dont sum
     */
    long countTendersContractor(Long idUser) throws ServiceException;

    /**
     * Collect all enums and cpv codes for list
     *
     * @return dto with list
     * @throws ServiceException
     */
    AddTenderDTO collectTenderConstant() throws ServiceException;
}