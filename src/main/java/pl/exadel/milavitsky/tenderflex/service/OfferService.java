package pl.exadel.milavitsky.tenderflex.service;

import org.springframework.security.access.prepost.PreAuthorize;
import pl.exadel.milavitsky.tenderflex.dto.AddOfferDTO;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

import java.util.List;


/**
 *  Service layer use methods from repository layer
 */

public interface OfferService {

    public List<OfferDto> findOfferByIdTender(int page, int size, Long id) throws ServiceException, IncorrectArgumentException;


    OfferDto findById(Long id) throws ServiceException;

    @PreAuthorize("hasAuthority('BIDDER')")
    OfferDto create(OfferDto offerDTO) throws ServiceException;

    OfferDto update(OfferDto offerDTO) throws ServiceException;

    long count() throws ServiceException;

    AddOfferDTO collectOfferConstant() throws ServiceException;
}
