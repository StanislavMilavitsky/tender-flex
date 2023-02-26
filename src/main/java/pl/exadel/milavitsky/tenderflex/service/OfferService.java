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

    Page<OfferDto> findOffersByIdContractor(Pageable pageable, Long idUser) throws ServiceException, IncorrectArgumentException;

    OfferDto findByIdContractor(Long id) throws ServiceException;

    OfferDto create(OfferDto offerDTO) throws ServiceException;

    AddOfferDTO collectOfferConstant() throws ServiceException;

    Page<OffersTenderBidderDto> findAllByBidder(Pageable pageable, Long idUser) throws ServiceException, IncorrectArgumentException;

    OffersTenderBidderDto findByIdBidder(Long id) throws ServiceException;

    OfferDto updateRejectByContractor(Long id) throws ServiceException;

    OfferDto updateApprovedByContractor(Long id) throws ServiceException;

    OfferDto updateApprovedByBidder(Long id) throws ServiceException;

    OfferDto updateDeclinedByBidder(Long id) throws ServiceException;
}
