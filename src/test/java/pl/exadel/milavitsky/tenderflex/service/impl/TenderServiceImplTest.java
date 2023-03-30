package pl.exadel.milavitsky.tenderflex.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.repository.TenderRepository;
import pl.exadel.milavitsky.tenderflex.service.TenderService;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class TenderServiceImplTest {

    @Mock
    private TenderRepository repository;

    private TenderService service;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        service = new TenderServiceImpl(repository, new ModelMapper());
        pageable = PageRequest.of(1, 1);
    }

    @Test
    void testCreatePositive() throws RepositoryException, ServiceException {
        Tender tender = Tender.builder().build();
        tender.setName("Company");
        when(repository.create(any())).thenReturn(tender);
        TenderDto tenderDto = TenderDto.builder().build();
        tenderDto.setName("Company");
        assertEquals(tenderDto, service.create(tenderDto));
    }

    @Test
    void testCreateNegative() throws RepositoryException, ServiceException {
        Tender tender = Tender.builder().build();
        tender.setName("Comp");
        when(repository.create(any())).thenReturn(tender);
        TenderDto tenderDto = TenderDto.builder().build();
        tenderDto.setName("Company");
        assertNotEquals(tenderDto, service.create(tenderDto));
    }

    @Test
    void testCreateException() throws RepositoryException {
        when(repository.create(any())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.create(new TenderDto()), "Add tender by title= null exception!");
        assertEquals(exception.getMessage(), "Add tender by title= null exception!");
    }

    @Test
    void testFindAllByBidderPositive() throws RepositoryException, ServiceException {
        when(repository.countOfEntity()).thenReturn(1L);
        when(repository.findAllByBidder(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<TenderDto> actual = service.findAllByBidder(pageable, 1L);
        Page<TenderDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 1L);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllByBidderNegative() throws RepositoryException, ServiceException {
        when(repository.countOfEntity()).thenReturn(1L);
        when(repository.findAllByBidder(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<TenderDto> actual = service.findAllByBidder(pageable, 1L);
        Page<TenderDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 2L);
        assertNotEquals(expected, actual);
    }

    @Test
    void testFindAllByBidderException() {
        when(repository.countOfEntity()).thenThrow(new DataAccessException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findAllByBidder(pageable, 1L),
                "Find all tenders by bidder service exception!");
        assertEquals(exception.getMessage(), "Find all tenders by bidder service exception!");
    }

    @Test
    void testFindAllByContractorPositive() throws RepositoryException, ServiceException {
        when(repository.countOfEntity()).thenReturn(1L);
        when(repository.findAllByContractor(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<TenderDto> actual = service.findAllByContractor(pageable, 1L);
        Page<TenderDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 1L);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllByContractorNegative() throws RepositoryException, ServiceException {
        when(repository.countOfEntity()).thenReturn(1L);
        when(repository.findAllByContractor(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<TenderDto> actual = service.findAllByContractor(pageable, 1L);
        Page<TenderDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 2L);
        assertNotEquals(expected, actual);
    }

    @Test
    void testFindAllByContractorException() {
        when(repository.countOfEntity()).thenThrow(new DataAccessException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findAllByContractor(pageable, 1L),
                "Find all tenders by contractor contractor service exception!");
        assertEquals(exception.getMessage(),
                "Find all tenders by contractor contractor service exception!");
    }

    @Test
    void testFindByIdPositive() throws RepositoryException, ServiceException {
        when(repository.findById(anyLong())).thenReturn(new Tender());
        TenderDto actual = service.findById(1L);
        assertEquals(new TenderDto(), actual);
    }

    @Test
    void testFindByIdNegative() throws RepositoryException, ServiceException {
        when(repository.findById(anyLong())).thenReturn(new Tender());
        TenderDto actual = service.findById(1L);
        TenderDto expected = TenderDto.builder().contract("231").build();
        assertNotEquals(expected, actual);
    }

    @Test
    void testFindByIdException() throws RepositoryException {
        when(repository.findById(anyLong())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findById(1L), "Cant find tender by id=1 !");
        assertEquals(exception.getMessage(), "Cant find tender by id=1 !");
    }

    @Test
    void testCountTendersContractorPositive() throws ServiceException {
        when(repository.countOfTendersContractor(anyLong())).thenReturn(1L);
        long actual = service.countTendersContractor(1L);
        assertEquals(1L, actual);
    }

    @Test
    void testCountTendersContractorNegative() throws ServiceException {
        when(repository.countOfTendersContractor(anyLong())).thenReturn(1L);
        long actual = service.countTendersContractor(1L);
        assertNotEquals(2L, actual);
    }

    @Test
    void testCountTendersContractorException() {
        when(repository.countOfTendersContractor(anyLong())).thenThrow(new DataAccessException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.countTendersContractor(1L), "Count tenders service exception!");
        assertEquals(exception.getMessage(), "Count tenders service exception!");
    }

    @Test
    void testCollectTenderConstantPositive() throws ServiceException {
        when(repository.findAllCPVCodes()).thenReturn(new ArrayList<>());
        AddTenderDTO expected = AddTenderDTO.builder()
                .countryList(Arrays.asList(Country.values()))
                .typeOfTenders(Arrays.asList(TypeOfTender.values()))
                .currencies(Arrays.asList(Currency.values()))
                .cpvCodes(new ArrayList<>())
                .build();
        AddTenderDTO actual = service.collectTenderConstant();
        assertEquals(expected, actual);
    }

    @Test
    void testCollectTenderConstantNegative() throws ServiceException {
        when(repository.findAllCPVCodes()).thenReturn(new ArrayList<>());
        AddTenderDTO expected = AddTenderDTO.builder()
                .countryList(Arrays.asList(Country.values()))
                .typeOfTenders(Arrays.asList(TypeOfTender.values()))
                .cpvCodes(new ArrayList<>())
                .build();
        AddTenderDTO actual = service.collectTenderConstant();
        assertNotEquals(expected, actual);
    }

    @Test
    void testCollectTenderConstantException() {
        when(repository.findAllCPVCodes()).thenThrow(new DataAccessException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.collectTenderConstant(), "Find constant tenders service exception!");
        assertEquals(exception.getMessage(), "Find constant tenders service exception!");
    }
}