package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.exadel.milavitsky.tenderflex.dto.AddOfferDTO;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.repository.OfferRepository;
import pl.exadel.milavitsky.tenderflex.service.OfferService;
import pl.exadel.milavitsky.tenderflex.service.Page;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public List<OfferDto> findOfferByIdTender(int page, int size, Long id) throws ServiceException, IncorrectArgumentException {
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
    public OfferDto findById(Long id) throws ServiceException {
        return null;
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public OfferDto create(OfferDto offerDTO) throws ServiceException {
        try {
            return offerRepository.create(offerDTO);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Add tender by title= %s exception!", offerDTO.getIdTender());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public OfferDto update(OfferDto offerDTO) throws ServiceException {
        return null;
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

    @Override
    public AddOfferDTO collectOfferConstant() throws ServiceException {
        try {
            AddOfferDTO addOfferDTO = new AddOfferDTO();
            addOfferDTO.setCountryList(Arrays.asList(Country.values()));
            addOfferDTO.setCurrencies(Arrays.asList(Currency.values()));
            return addOfferDTO;
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find constant offer service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);

        }

    }
}
