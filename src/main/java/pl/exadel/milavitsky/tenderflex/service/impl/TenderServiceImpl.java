package pl.exadel.milavitsky.tenderflex.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.exadel.milavitsky.tenderflex.dto.CreateTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.PublishTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
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
    private final Mapper<TenderDTO, Tender> mapper;

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR') and  hasAuthority('BIDDER')")
    public TenderDTO findById(Long id) throws ServiceException {
        try {
            TenderDTO TenderDTO = mapper.toDTO(tenderRepository.findById(id));
            return TenderDTO;
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Cant find tender by id=%d !", id);
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public PublishTenderDTO create(PublishTenderDTO publishTenderDTO) throws ServiceException {
        try {
            Tender tender = mapper.fromDTO(publishTenderDTO);
            tender = tenderRepository.create(tender);
            return mapper.toDTO(tender);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Add tender by title= %s exception!", publishTenderDTO.getName());
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public TenderDTO update(TenderDTO tenderDTO) throws ServiceException {
        try {
            Tender tender = tenderRepository.update(mapper.fromDTO(tenderDTO));
            return mapper.toDTO(tender);
        } catch (RepositoryException exception) {
            String exceptionMessage = String.format("Update tender by title= %s exception!", tenderDTO);
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
    public List<TenderDTO> findAll(int page, int size) throws ServiceException, IncorrectArgumentException {
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
    public List<TenderDTO> searchByTitleOrDescription(String part) throws ServiceException {
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
    public List<TenderDTO> sortByTitle(SortType sortType) throws ServiceException {
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
    public List<TenderDTO> sortByDateStart(SortType sortType) throws ServiceException {
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
    public List<TenderDTO> sortByDateEnd(SortType sortType) throws ServiceException {
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
    public List<TenderDTO> findAllByContractor(int page, int size, String contractorCompany) throws ServiceException, IncorrectArgumentException {
        try {
            long count = countTendersContractor(contractorCompany);
            Page userPage = new Page(page, size, count);
            List<Tender> tenders = tenderRepository.findAllTendersContractor(userPage.getOffset(), userPage.getLimit(), contractorCompany);
            return tenders.stream().map(mapper::toDTO).collect(Collectors.toList());
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find all tenders contractor service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public long countTendersContractor(String contractorCompany) throws ServiceException {
        try {
            return tenderRepository.countOfTendersContractor(contractorCompany);
        } catch (DataAccessException exception) {
            String exceptionMessage = "Count tenders service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);
        }
    }

    @Override
    public CreateTenderDTO collectTenderConstant() throws ServiceException {
        try {
            CreateTenderDTO createTenderDTO = new CreateTenderDTO();
            createTenderDTO.setCountryList(Arrays.asList(Country.values()));
            createTenderDTO.setTypeOfTenders(Arrays.asList(TypeOfTender.values()));
            createTenderDTO.setCurrencies(Arrays.asList(Currency.values()));
            createTenderDTO.setCpvCodes(tenderRepository.findAllCPVCodes());
            return createTenderDTO;
        } catch (DataAccessException exception) {
            String exceptionMessage = "Find constant tenders service exception!";
            log.error(exceptionMessage, exception);
            throw new ServiceException(exceptionMessage, exception);

        }
    }
}