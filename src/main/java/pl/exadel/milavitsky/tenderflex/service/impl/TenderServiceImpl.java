package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusTender;
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.mapper.Mapper;
import pl.exadel.milavitsky.tenderflex.repository.TenderRepository;
import pl.exadel.milavitsky.tenderflex.service.TenderService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TenderServiceImpl implements TenderService {

    private final TenderRepository tenderRepository;

    private final Mapper<TenderDto, Tender> mapper;

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public TenderDto create(TenderDto tenderDto) throws ServiceException {
        try {
            Tender tender = mapper.fromDTO(tenderDto);
            tender.setStatusTender(StatusTender.IN_PROGRESS);
            tender.setPublicationDate(LocalDate.now());
            tender = tenderRepository.create(tender);
            return mapper.toDTO(tender);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Add tender by title= %s exception!", tenderDto.getName());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PostAuthorize("hasAuthority('BIDDER')")
    public Page<TenderDto> findAllByBidder(Pageable pageable, Long id) throws ServiceException, IncorrectArgumentException {
        try {
            long count = tenderRepository.countOfEntity();
            List<Tender> tenders = tenderRepository.findAllById(pageable, id);
            List<TenderDto> tenderDtos = tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
            return new PageImpl<>(tenderDtos,pageable,count);
        } catch (DataAccessException|RepositoryException exception) {
            String exceptionMessage = "Find all tenders by bidder service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }


    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public Page<TenderDto> findAllByContractor(Pageable pageable, Long id) throws ServiceException, IncorrectArgumentException {
        try {
            long count = tenderRepository.countOfEntity();
            List<Tender> tenders = tenderRepository.findAllById(pageable, id);
            List<TenderDto> tenderDtos = tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
            return new PageImpl<>(tenderDtos,pageable,count);
        } catch (DataAccessException|RepositoryException exception) {
            String exceptionMessage = "Find all tenders by contractor contractor service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR') and  hasAuthority('BIDDER')")
    public TenderDto findById(Long id) throws ServiceException {
        try {
            TenderDto tenderDto = mapper.toDTO(tenderRepository.findById(id));
            return tenderDto;
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find tender by id=%d !", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }



    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public long countTendersContractor(Long idUser) throws ServiceException {
        try {
            return tenderRepository.countOfTendersContractor(idUser);
        } catch (DataAccessException exception) {
            String exceptionMessage = "Count tenders service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public AddTenderDTO collectTenderConstant() throws ServiceException {
        try {
            AddTenderDTO addTenderDTO = new AddTenderDTO();
            addTenderDTO.setCountryList(Arrays.asList(Country.values()));
            addTenderDTO.setTypeOfTenders(Arrays.asList(TypeOfTender.values()));
            addTenderDTO.setCurrencies(Arrays.asList(Currency.values()));
            addTenderDTO.setCpvCodes(tenderRepository.findAllCPVCodes());
            return addTenderDTO;
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find constant tenders service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);

        }
    }

}