package pl.exadel.milavitsky.tenderflex.service;

import pl.exadel.milavitsky.tenderflex.dto.AddOfferDTO;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

import java.util.List;


/**
 *  Service layer use methods from repository layer
 */

public interface OfferService {

    List<OfferDto> findOfferByIdTender(int page, int size, Long id) throws ServiceException, IncorrectArgumentException;

    OfferDto findByIdContractor(Long id) throws ServiceException;

    OfferDto create(OfferDto offerDTO) throws ServiceException;

    long count() throws ServiceException;

    AddOfferDTO collectOfferConstant() throws ServiceException;

    List<OffersTenderBidderDto> findAllByBidder(int page, int size, Long idUser) throws ServiceException, IncorrectArgumentException;

    OffersTenderBidderDto findByIdBidder(Long id) throws ServiceException;

    OfferDto updateRejectByContractor(OfferDto offerDto) throws ServiceException;

    OfferDto updateApprovedByContractor(OfferDto offerDto) throws ServiceException;

    OfferDto updateApprovedByBidder(OfferDto offerDto) throws ServiceException;

    OfferDto updateDeclinedByBidder(OfferDto offerDto) throws ServiceException;
}
