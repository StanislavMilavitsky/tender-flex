package pl.exadel.milavitsky.tenderflex.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import pl.exadel.milavitsky.tenderflex.dto.AddOfferDTO;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;



/**
 *  Service layer use methods from repository layer
 */

public interface OfferService {

    /**
     * Find offers by id
     *
     * @param pageable size and page of view
     * @param idUser
     * @return list of offer dto
     * @throws ServiceException
     * @throws IncorrectArgumentException
     */
    Page<OfferDto> findOffersByContractor(Pageable pageable, Long idUser) throws ServiceException, IncorrectArgumentException;

    /**
     * Find offers by id bidder
     *
     * @param pageable page and size of view
     * @param idUser
     * @return list of offers with tenders
     * @throws ServiceException
     * @throws IncorrectArgumentException
     */
    Page<OffersTenderBidderDto> findOffersByBidder(Pageable pageable, Long idUser) throws ServiceException, IncorrectArgumentException;

    /**
     * Find offers by id bidder
     *
     * @param id offer
     * @return entity offer dto
     * @throws ServiceException
     */
    OfferDto findByIdContractor(Long id) throws ServiceException;

    /**
     * Find offer by id bidder
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    OffersTenderBidderDto findByIdBidder(Long id) throws ServiceException;

    /**
     * Create offer in data base
     *
     * @param offerDTO entity offer
     * @return entity offer dto
     * @throws ServiceException
     */
    OfferDto create(OfferDto offerDTO) throws ServiceException;

    /**
     * Collect all enums entity
     *
     * @return add offer dto with list of fields from enums
     * @throws ServiceException
     */
    AddOfferDTO collectOfferConstant() throws ServiceException;

    /**
     * Update status offer reject by contractor
     *
     * @param id offer
     * @return updated entity
     * @throws ServiceException
     */
    OfferDto updateRejectByContractor(Long id) throws ServiceException;

    /**
     * Update status approved by contractor
     *
     * @param id offer
     * @return updated entity
     * @throws ServiceException
     */
    OfferDto updateApprovedByContractor(Long id) throws ServiceException;

    /**
     * Update status offer approved by bidder and closed status for tender
     *
     * @param id offer
     * @return updated entity
     * @throws ServiceException
     */
    OfferDto updateApprovedByBidder(Long id) throws ServiceException;

    /**
     * Update status offer declined by bidder
     *
     * @param id offer
     * @return updated entity
     * @throws ServiceException
     */
    OfferDto updateDeclinedByBidder(Long id) throws ServiceException;
}
