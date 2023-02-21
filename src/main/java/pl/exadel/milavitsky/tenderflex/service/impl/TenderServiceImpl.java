package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
import pl.exadel.milavitsky.tenderflex.service.Page;
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
    public List<TenderDto> findAllByBidder(int page, int size, Long idUser) throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page userPage = new Page(page, size, count);
            List<Tender> tenders = tenderRepository.findAllByBidder(userPage.getOffset(), userPage.getLimit(), idUser);
            return tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find all tenders by bidder service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }

    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public List<TenderDto> findAllByContractor(int page, int size, Long idUser) throws ServiceException, IncorrectArgumentException {
        try {
            long count = countTendersContractor(idUser);
            Page userPage = new Page(page, size, count);
            List<Tender> tenders = tenderRepository.findAllByContractor(userPage.getOffset(), userPage.getLimit(), idUser);
            return tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (DataAccessException exception) {
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
    public long count() throws ServiceException {
        try {
            return tenderRepository.countOfEntity();
        } catch (DataAccessException exception) {
            String exceptionMessage = "Count tenders service exception!";
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