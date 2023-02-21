package pl.exadel.milavitsky.tenderflex.service;

import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

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
    List<TenderDto> findAllByBidder(int page, int size, Long isUser) throws ServiceException, IncorrectArgumentException;

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