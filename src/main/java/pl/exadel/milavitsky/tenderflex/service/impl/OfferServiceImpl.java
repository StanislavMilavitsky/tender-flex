package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.exadel.milavitsky.tenderflex.config.MapperUtil;
import pl.exadel.milavitsky.tenderflex.dto.AddOfferDTO;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.repository.OfferRepository;
import pl.exadel.milavitsky.tenderflex.service.OfferService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    private final ModelMapper modelMapper;

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public Page<OfferDto> findOffersByContractor(Pageable pageable, Long idUser) throws ServiceException {
        try {
            long count = offerRepository.countOfOffersByContractor(idUser);
            List<Offer>  offers = offerRepository.findAllOffersByIdContractor(pageable, idUser);
            List<OfferDto> offerDtos = MapperUtil.convertList(offers, this::convertToOfferDto);
            return new PageImpl<>(offerDtos,pageable,count);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find offer by id=%d tender!", idUser);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public Page<OffersTenderBidderDto> findOffersByBidder(Pageable pageable, Long idUser) throws ServiceException {
        try {
            long count = offerRepository.countOfOffersByBidder(idUser);
            List<OffersTenderBidderDto> offersTenderBidderDtos = offerRepository.findAllOffersByBidder(pageable, idUser);
            return new PageImpl<>(offersTenderBidderDtos,pageable,count);
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
            Offer offer = modelMapper.map(offerDTO, Offer.class);
            offer = offerRepository.create(offer);
            return modelMapper.map(offer, OfferDto.class);
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
            return modelMapper.map(offerRepository.findByIdContractor(id), OfferDto.class);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find offer by id=%d !", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
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
    public OfferDto updateRejectByContractor(Long id) throws ServiceException {
        try {
            Offer offer = offerRepository.updateRejectByContractor(id);
            return modelMapper.map(offer, OfferDto.class);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update status  by = %d exception!", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public OfferDto updateApprovedByContractor(Long id) throws ServiceException {
        try {
            Offer offer = offerRepository.updateApprovedByContractor(id);
            return modelMapper.map(offer, OfferDto.class);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update status  by = %d exception!", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
         }
    }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public OfferDto updateApprovedByBidder(Long id) throws ServiceException {
            try {
                Offer offer = offerRepository.updateApprovedByBidder(id);
                return modelMapper.map(offer, OfferDto.class);
            } catch (RepositoryException exception) {
                String exceptionMessage = String.format("Update status  by = %d exception!", id);
                log.error(exceptionMessage, exception);
                throw new ServiceException(exceptionMessage, exception);
            }
        }

    @Override
    @PreAuthorize("hasAuthority('BIDDER')")
    public OfferDto updateDeclinedByBidder(Long id) throws ServiceException {
        try {
            Offer offer = offerRepository.updateDeclinedByBidder(id);
            return modelMapper.map(offer, OfferDto.class);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update status  by = %d exception!", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }


    @Override
    public AddOfferDTO collectOfferConstant() throws ServiceException {
        try {

            return AddOfferDTO.builder()
                    .countryList(Arrays.asList(Country.values()))
                    .currencies(Arrays.asList(Currency.values())).build();
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find constant offer service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);

        }
    }

    private OfferDto convertToOfferDto(Offer offer) {
        OfferDto offerDto = modelMapper.map(offer, OfferDto.class);
        return offerDto;
    }

}
