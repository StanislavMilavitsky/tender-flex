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

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class OfferServiceImplTest {

    @Mock
    private OfferRepository repository;

    private OfferService service;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        service = new OfferServiceImpl(repository, new ModelMapper());
        pageable = PageRequest.of(1, 1);
    }

    @Test
    void testFindOffersByContractorPositive() throws RepositoryException, ServiceException {
        when(repository.countOfOffersByContractor(anyLong())).thenReturn(2L);
        when(repository.findAllOffersByIdContractor(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<OfferDto> actual = service.findOffersByContractor(pageable, 1L);
        Page<OfferDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 2L);
        assertEquals(expected, actual);
    }

    @Test
    void testFindOffersByContractorNegative() throws RepositoryException, ServiceException {
        when(repository.countOfOffersByContractor(anyLong())).thenReturn(2L);
        when(repository.findAllOffersByIdContractor(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<OfferDto> actual = service.findOffersByContractor(pageable, 1L);
        Page<OfferDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 1L);
        assertNotEquals(expected, actual);
    }

    @Test
    void testFindOffersByContractorException() {
        when(repository.countOfOffersByContractor(anyLong())).thenThrow(new DataAccessException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findOffersByContractor(pageable, 1L),
                "Cant find offer by id=1 tender!");
        assertEquals(exception.getMessage(), "Cant find offer by id=1 tender!");
    }

    @Test
    void testFindOffersByBidderPositive() throws RepositoryException, ServiceException {
        when(repository.countOfOffersByBidder(anyLong())).thenReturn(2L);
        when(repository.findAllOffersByBidder(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<OffersTenderBidderDto> actual = service.findOffersByBidder(pageable, 1L);
        Page<OffersTenderBidderDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 2L);
        assertEquals(expected, actual);
    }

    @Test
    void testFindOffersByBidderNegative() throws RepositoryException, ServiceException {
        when(repository.countOfOffersByBidder(anyLong())).thenReturn(2L);
        when(repository.findAllOffersByBidder(pageable, 1L)).thenReturn(new ArrayList<>());
        Page<OffersTenderBidderDto> actual = service.findOffersByBidder(pageable, 1L);
        Page<OffersTenderBidderDto> expected = new PageImpl<>(new ArrayList<>(), pageable, 1L);
        assertNotEquals(expected, actual);
    }

    @Test
    void testFindOffersByBidderException() {
        when(repository.countOfOffersByBidder(anyLong())).thenThrow(new DataAccessException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findOffersByBidder(pageable, 1L),
                "Cant find offer by user id=1!");
        assertEquals(exception.getMessage(), "Cant find offer by user id=1!");
    }

    @Test
    void testCreatePositive() throws RepositoryException, ServiceException {
        when(repository.create(any())).thenReturn(new Offer());
        OfferDto actual = service.create(new OfferDto());
        assertEquals(new OfferDto(), actual);
    }

    @Test
    void testCreateNegative() throws RepositoryException, ServiceException {
        when(repository.create(any())).thenReturn(new Offer());
        OfferDto actual = service.create(new OfferDto());
        assertNotEquals(OfferDto.builder().id(2L).build(), actual);
    }

    @Test
    void testCreateException() throws RepositoryException {
        when(repository.create(any())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.create(new OfferDto()),
                "Add offer by official name = null exception!");
        assertEquals(exception.getMessage(), "Add offer by official name = null exception!");
    }

    @Test
    void testFindByIdContractorPositive() throws RepositoryException, ServiceException {
        when(repository.findByIdContractor(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.findByIdContractor(1L);
        assertEquals(new OfferDto(), actual);
    }

    @Test
    void testFindByIdContractorNegative() throws RepositoryException, ServiceException {
        when(repository.findByIdContractor(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.findByIdContractor(1L);
        assertNotEquals(OfferDto.builder().id(2L).build(), actual);
    }

    @Test
    void testFindByIdContractorException() throws RepositoryException {
        when(repository.findByIdContractor(anyLong())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findByIdContractor(1L),
                "Cant find offer by id=1 !");
        assertEquals(exception.getMessage(), "Cant find offer by id=1 !");
    }

    @Test
    void testFindByIdBidderPositive() throws RepositoryException, ServiceException {
        when(repository.findByIdBidder(anyLong())).thenReturn(new OffersTenderBidderDto());
        OffersTenderBidderDto actual = service.findByIdBidder(1L);
        assertEquals(new OffersTenderBidderDto(), actual);
    }

    @Test
    void testFindByIdBidderNegative() throws RepositoryException, ServiceException {
        when(repository.findByIdBidder(anyLong())).thenReturn(new OffersTenderBidderDto());
        OffersTenderBidderDto actual = service.findByIdBidder(1L);
        assertNotEquals(OffersTenderBidderDto.builder().id(2L).build(), actual);
    }

    @Test
    void testFindByIdBidderException() throws RepositoryException {
        when(repository.findByIdBidder(anyLong())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.findByIdBidder(1L),
                "Cant find offer by id=1 !");
        assertEquals(exception.getMessage(), "Cant find offer by id=1 !");
    }

    @Test
    void testUpdateRejectByContractorPositive() throws RepositoryException, ServiceException {
        when(repository.updateRejectByContractor(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateRejectByContractor(1L);
        assertEquals(new OfferDto(), actual);
    }

    @Test
    void testUpdateRejectByContractorNegative() throws RepositoryException, ServiceException {
        when(repository.updateRejectByContractor(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateRejectByContractor(1L);
        assertNotEquals(OfferDto.builder().id(2L).build(), actual);
    }

    @Test
    void testUpdateRejectByContractorException() throws RepositoryException {
        when(repository.updateRejectByContractor(anyLong())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.updateRejectByContractor(1L),
                "Update status  by = 1 exception!");
        assertEquals(exception.getMessage(), "Update status  by = 1 exception!");
    }

    @Test
    void testUpdateApprovedByContractorPositive() throws RepositoryException, ServiceException {
        when(repository.updateApprovedByContractor(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateApprovedByContractor(1L);
        assertEquals(new OfferDto(), actual);
    }

    @Test
    void testUpdateApprovedByContractorNegative() throws RepositoryException, ServiceException {
        when(repository.updateApprovedByContractor(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateApprovedByContractor(1L);
        assertNotEquals(OfferDto.builder().id(2L).build(), actual);
    }

    @Test
    void testUpdateApprovedByContractorException() throws RepositoryException {
        when(repository.updateApprovedByContractor(anyLong())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.updateApprovedByContractor(1L),
                "Update status  by = 1 exception!");
        assertEquals(exception.getMessage(), "Update status  by = 1 exception!");
    }

    @Test
    void testUpdateApprovedByBidderPositive() throws RepositoryException, ServiceException {
        when(repository.updateApprovedByBidder(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateApprovedByBidder(1L);
        assertEquals(new OfferDto(), actual);
    }

    @Test
    void testUpdateApprovedByBidderNegative() throws RepositoryException, ServiceException {
        when(repository.updateApprovedByBidder(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateApprovedByBidder(1L);
        assertNotEquals(OfferDto.builder().id(2L).build(), actual);
    }

    @Test
    void testUpdateApprovedByBidderException() throws RepositoryException {
        when(repository.updateApprovedByBidder(anyLong())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.updateApprovedByBidder(1L),
                "Update status  by = 1 exception!");
        assertEquals(exception.getMessage(), "Update status  by = 1 exception!");
    }

    @Test
    void testUpdateDeclinedByBidderPositive() throws RepositoryException, ServiceException {
        when(repository.updateDeclinedByBidder(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateDeclinedByBidder(1L);
        assertEquals(new OfferDto(), actual);
    }

    @Test
    void testUpdateDeclinedByBidderNegative() throws RepositoryException, ServiceException {
        when(repository.updateDeclinedByBidder(anyLong())).thenReturn(new Offer());
        OfferDto actual = service.updateDeclinedByBidder(1L);
        assertNotEquals(OfferDto.builder().id(2L).build(), actual);
    }

    @Test
    void testUpdateDeclinedByBidderException() throws RepositoryException {
        when(repository.updateDeclinedByBidder(anyLong())).thenThrow(new RepositoryException("...") {
        });
        ServiceException exception = assertThrows(ServiceException.class,
                () -> service.updateDeclinedByBidder(1L),
                "Update status  by = 1 exception!");
        assertEquals(exception.getMessage(), "Update status  by = 1 exception!");
    }

    @Test
    void testCollectOfferConstantPositive() throws ServiceException {
        AddOfferDTO actual = service.collectOfferConstant();
        AddOfferDTO expected = AddOfferDTO.builder()
                .countryList(Arrays.asList(Country.values()))
                .currencies(Arrays.asList(Currency.values())).build();
        assertEquals(expected, actual);
    }

    @Test
    void testCollectOfferConstantNegative() throws ServiceException {
        AddOfferDTO actual = service.collectOfferConstant();
        assertNotEquals(new AddOfferDTO(), actual);
    }
}