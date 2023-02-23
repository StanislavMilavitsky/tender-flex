package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.exadel.milavitsky.tenderflex.dto.AddOfferDTO;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusOffer;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.mapper.Mapper;
import pl.exadel.milavitsky.tenderflex.repository.OfferRepository;
import pl.exadel.milavitsky.tenderflex.service.OfferService;
import pl.exadel.milavitsky.tenderflex.service.Page;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    private final Mapper<OfferDto, Offer> mapper;

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public List<OfferDto> findOfferByIdTender(int page, int size, Long id) throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page offerPage = new Page(page, size, count);
            List<Offer>  offers = offerRepository.findAllOffersByIdTender(offerPage.getOffset(), offerPage.getLimit(), id);
            return offers.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find offer by id=%d tender!", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public List<OffersTenderBidderDto> findAllByBidder(int page, int size, Long idUser) throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page offerPage = new Page(page, size, count);
            return offerRepository.findAllOffersByBidder(offerPage.getOffset(), offerPage.getLimit(), idUser);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find offer by user id=%d!", idUser);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public OfferDto create(OfferDto offerDTO) throws ServiceException {
        try {
            offerDTO.setStatus("OFFER_SENT");
            offerDTO.setSentDate(LocalDate.now().toString());
            Offer offer = mapper.fromDTO(offerDTO);
            offer = offerRepository.create(offer);
            return mapper.toDTO(offer);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Add offer by official name = %s exception!", offerDTO.getOfficialName());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public OfferDto findByIdContractor(Long id) throws ServiceException {
        try {
            OfferDto OfferDto = mapper.toDTO(offerRepository.findByIdContractor(id));
            return OfferDto;
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find offer by id=%d !", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public OffersTenderBidderDto findByIdBidder(Long id) throws ServiceException  {
        try {
            return offerRepository.findByIdBidder(id);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find offer by id=%d !", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public OfferDto updateRejectByContractor(OfferDto offerDto) throws ServiceException {
        try {
            Offer offer = offerRepository.updateRejectByContractor(mapper.fromDTO(offerDto));
            return mapper.toDTO(offer);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update status = %s exception!", offerDto.getStatus());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public OfferDto updateApprovedByContractor(OfferDto offerDto) throws ServiceException {
        try {
            Offer offer = offerRepository.updateApprovedByContractor(mapper.fromDTO(offerDto));
            return mapper.toDTO(offer);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update status = %s exception!", offerDto.getStatus());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
         }
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public OfferDto updateApprovedByBidder(OfferDto offerDto) throws ServiceException {
            try {
                Offer offer = offerRepository.updateApprovedByBidder(mapper.fromDTO(offerDto));
                return mapper.toDTO(offer);
            } catch (RepositoryException exception) {
                String exceptionMessage = String.format("Update status = %s exception!", offerDto.getStatus());
                log.error(exceptionMessage, exception);
                throw new ServiceException(exceptionMessage, exception);
            }
        }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public OfferDto updateDeclinedByBidder(OfferDto offerDto) throws ServiceException {
        try {
            Offer offer = offerRepository.updateDeclinedByBidder(mapper.fromDTO(offerDto));
            return mapper.toDTO(offer);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update status = %s exception!", offerDto.getStatus());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
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
