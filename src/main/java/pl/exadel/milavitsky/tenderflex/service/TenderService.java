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

    TenderDto findById(Long id) throws ServiceException;

    TenderDto create(TenderDto tenderDto) throws ServiceException;

    /**
     * Use method findAll in repository layer
     *
     * @return list of all entity
     */
    Page<TenderDto> findAllByBidder(Pageable pageable, Long id) throws ServiceException, IncorrectArgumentException;


    /**
     * Use method findAllTenderContractor
     *
     * @param
     * @return list of all tenders of contractor
     */
    Page<TenderDto> findAllByContractor(Pageable pageable, Long id) throws ServiceException, IncorrectArgumentException;

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