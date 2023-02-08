package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.repository.OfferRepository;
import pl.exadel.milavitsky.tenderflex.service.OfferService;
import pl.exadel.milavitsky.tenderflex.service.Page;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public List<Offer> findOfferByIdTender(int page, int size, Long id) throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page offerPage = new Page(page, size, count);
            return offerRepository.findAllOffersByIdTender(page, size, id);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find offer by id=%d tender!", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public Offer findById(Long id) throws ServiceException {
        return null;
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public Offer create(Offer offer) throws ServiceException {
        try {
            return offerRepository.create(offer);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Add tender by title= %s exception!", offer.getIdTender());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public Offer update(Offer offer) throws ServiceException {
        return null;
    }

    @Override
    public void deleteById(Long id) throws ServiceException {

    }

    @Override
    public long count() throws ServiceException {
        try {
            return offerRepository.countOfEntity();
        } catch (DataAccessException exception) {
            String exceptionMessage = "Count offers service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }
}
