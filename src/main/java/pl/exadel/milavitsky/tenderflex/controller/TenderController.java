package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.exception.ControllerException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.service.TenderService;

import javax.validation.Valid;


/**
 * Tender Rest Controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tender")
public class TenderController implements BindingResultHandler{

    private final TenderService tenderService;

    /**
     * Collect all enums fields from entity
     *
     * @return list of enums
     * @throws ServiceException the service exception
     */
    @GetMapping("/menu-tender")
    public ResponseEntity<AddTenderDTO> add()
            throws ServiceException {
            AddTenderDTO result = tenderService.collectTenderConstant();
            return  ResponseEntity.ok(result);
        }


    /**
     * Create tender.
     *
     * @param tenderDto the tender dto
     * @return the response entity
     * @throws ServiceException    the service exception
     * @throws ControllerException if entity fields not valid
     */

    @PostMapping()
    public ResponseEntity<TenderDto> create(@RequestBody @Valid TenderDto tenderDto, BindingResult bindingResult)
            throws ServiceException, ControllerException {
        if (bindingResult.hasErrors()) {
            TenderDto result = tenderService.create(tenderDto);
            return ResponseEntity.ok(result);
        } else {
            log.error(bindingResultHandler(bindingResult));
            throw new ControllerException(bindingResultHandler(bindingResult));
        }
    }


    /**
     * Find tender by id.
     *
     * @param id the id tender
     * @return the response entity
     * @throws ServiceException  the service exception
     */
    @GetMapping()
    public ResponseEntity<TenderDto> findById(@RequestParam(name = "id") Long id) throws ServiceException {
            TenderDto tenderDto = tenderService.findById(id);
            return ResponseEntity.ok(tenderDto);
    }

    /**
     * Find all tenders by bidder
     *
     * @param id bidder
     * @param pageable meta data of view page
     * @return page of tender dto
     * @throws ServiceException the service exception
     * @throws IncorrectArgumentException
     */
    @GetMapping("all-bidder")
    public Page<TenderDto> findAllByBidder(
            @RequestParam(value = "id") Long id,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) throws ServiceException, IncorrectArgumentException {
        return tenderService.findAllByBidder(pageable, id);
    }

    /**
     * Find all tenders by contractor
     *
     * @param id contractor
     * @param pageable meta data of view page
     * @return page of tender dto
     * @throws ServiceException
     * @throws IncorrectArgumentException
     */
    @GetMapping("/all-contractor")
    public Page<TenderDto> findAllByContractor(
            @RequestParam(value = "id") Long id,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) throws ServiceException, IncorrectArgumentException {
        return tenderService.findAllByContractor(pageable, id);
    }

}
