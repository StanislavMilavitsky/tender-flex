package pl.exadel.milavitsky.tenderflex.service;

import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

import java.util.List;


/**
 *  Service layer use methods from repository layer
 */

public interface OfferService extends BaseService<Offer> {

    public List<Offer> findOfferByIdTender(int page, int size, Long id) throws ServiceException, IncorrectArgumentException;


    long count() throws ServiceException;
}
