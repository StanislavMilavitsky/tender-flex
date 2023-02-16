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
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.mapper.Mapper;
import pl.exadel.milavitsky.tenderflex.repository.TenderRepository;
import pl.exadel.milavitsky.tenderflex.service.Page;
import pl.exadel.milavitsky.tenderflex.service.TenderService;
import pl.exadel.milavitsky.tenderflex.validation.sort.SortType;

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
    public TenderDto create(TenderDto tenderDto) throws ServiceException {//todo user id
        try {
            Tender tender = mapper.fromDTO(tenderDto);
            tender = tenderRepository.create(tender);
            return mapper.toDTO(tender);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Add tender by title= %s exception!", tenderDto.getName());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public TenderDto update(TenderDto tenderDto) throws ServiceException {
        try {
            Tender tender = tenderRepository.update(mapper.fromDTO(tenderDto));
            return mapper.toDTO(tender);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update tender by title= %s exception!", tenderDto);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteById(Long id) throws ServiceException {
        try {
            tenderRepository.delete(id);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Delete tender by id=%d exception!", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PostAuthorize("hasAuthority('BIDDER')")
    public List<TenderDto> findAll(int page, int size) throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page userPage = new Page(page, size, count);
            List<Tender> tags = tenderRepository.findAll(userPage.getOffset(), userPage.getLimit());
            return tags.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find all tenders service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }

    }

    @Override
    public List<TenderDto> searchByTitleOrDescription(String part) throws ServiceException {
        try {
            List<Tender> Tenders = tenderRepository.searchByTitleOrDescription(part);
            return Tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Find tender by word=%s exception!", part);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public List<TenderDto> sortByTitle(SortType sortType) throws ServiceException {
        try {
            List<Tender> tenders = tenderRepository.sortByTitle(sortType);
            return tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (RepositoryException exception) {
            String exceptionMessage = "Sort tenders by title";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public List<TenderDto> sortByDateStart(SortType sortType) throws ServiceException {
        try {
            List<Tender> tenders = tenderRepository.sortByDateOfStart(sortType);
            return tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (RepositoryException exception) {
            String exceptionMessage = "Sort tenders by date of start";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public List<TenderDto> sortByDateEnd(SortType sortType) throws ServiceException {
        try {
            List<Tender> tenders = tenderRepository.sortByDateOfEnd(sortType);
            return tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (RepositoryException exception) {
            String exceptionMessage = "Sort tenders by date of start";
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
    public List<TenderDto> findAllByContractor(int page, int size, Long id_user) throws ServiceException, IncorrectArgumentException {
        try {
            long count = countTendersContractor(id_user);
            Page userPage = new Page(page, size, count);
            List<Tender> tenders = tenderRepository.findAllTendersContractor(userPage.getOffset(), userPage.getLimit(), id_user);
            return tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find all tenders contractor service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public long countTendersContractor(Long id_user) throws ServiceException {
        try {
            return tenderRepository.countOfTendersContractor(id_user);
        } catch (DataAccessException exception) {
            String exceptionMessage = "Count tenders service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
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