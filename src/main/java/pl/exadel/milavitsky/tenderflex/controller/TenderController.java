package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.exception.ControllerException;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
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
public class TenderController extends PageController<TenderDto> {

    private final TenderService tenderService;

    @GetMapping("/new")
    public ResponseEntity<AddTenderDTO> add()
            throws ServiceException {
            AddTenderDTO result = tenderService.collectTenderConstant();
            return  ResponseEntity.ok(result);
        }


    /**
     * Add tender.
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
            log.error(bindingResultHandler(bindingResult));
            throw new ControllerException(bindingResultHandler(bindingResult));
        } else {
            TenderDto result = tenderService.create(tenderDto);
            return ResponseEntity.ok(result);
        }
    }


    /**
     * Find tender by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping()
    public ResponseEntity<TenderDto> findById(@RequestParam(name = "id") Long id) throws ControllerException, ServiceException {
        if (id > 0) {
            TenderDto tenderDto = tenderService.findById(id);
            return ResponseEntity.ok(tenderDto);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

    @GetMapping("all-tenders")
    public Page<TenderDto> findAll(
            @RequestParam(value = "id") Long id,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) throws ServiceException, IncorrectArgumentException {
        return tenderService.findAllByBidder(pageable, id);
    }

    @GetMapping("/all-tenders/")
    public Page<TenderDto> findAllByContractor(
            @RequestParam(value = "id") Long id,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) throws ServiceException, IncorrectArgumentException {
        return tenderService.findAllByContractor(pageable, id);
    }

    @Override
    public ResponseEntity<PagedModel<TenderDto>> findAll(int page, int size) throws ServiceException, IncorrectArgumentException {
        return null;
    }
}
