package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.dto.AddOfferDTO;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
import pl.exadel.milavitsky.tenderflex.exception.ControllerException;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.service.OfferService;

import javax.validation.Valid;

/**
 * Offer Rest Controller
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offers")
public class OfferController implements BindingResultHandler {

    private final OfferService offerService;

    /**
     * Find tenders by id contractor.
     *
     * @param idUser the id user
     * @param pageable meta information of page
     * @return page with offers
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/contractor")
    public Page<OfferDto> findAllByContractor(@RequestParam(name = "idUser") Long idUser, @PageableDefault(page = 0, size = 20) Pageable pageable)
            throws ServiceException, IncorrectArgumentException {
        return offerService.findOffersByContractor(pageable, idUser);
    }

    /**
     * Find tenders by id contractor.
     *
     * @param idUser the id user
     * @param pageable meta information of page
     * @return page with offers
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/bidder")
    public Page<OffersTenderBidderDto> findAllByBidder(@RequestParam(value = "idUser") Long  idUser, Pageable pageable
    ) throws ServiceException, IncorrectArgumentException {
        return offerService.findOffersByBidder(pageable, idUser);
    }

    /**
     * Create offer and use method from service layer
     *
     * @param offerDTO offer entity
     * @param bindingResult for exceptions
     * @return offer dto
     * @throws ServiceException
     * @throws ControllerException
     */
    @PostMapping()
    public ResponseEntity<OfferDto> create(@RequestBody @Valid OfferDto offerDTO, BindingResult bindingResult)
            throws ServiceException, ControllerException {
        if (bindingResult.hasErrors()) {
            log.error(bindingResultHandler(bindingResult));
            throw new ControllerException(bindingResultHandler(bindingResult));
        } else {
            OfferDto result = offerService.create(offerDTO);
            return ResponseEntity.ok(result);
        }
    }

    /**
     * Find all enums fields and collect to lists
     *
     * @return list of enums
     * @throws ServiceException
     */
    @GetMapping("/menu-create")
    public ResponseEntity<AddOfferDTO> add()
            throws ServiceException {
        AddOfferDTO result = offerService.collectOfferConstant();
        return  ResponseEntity.ok(result);
    }

    /**
     * Find offer by id.
     *
     * @param id the id offer
     * @return the response entity
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/details-contractor")
    public ResponseEntity<OfferDto> findByIdContractor(@RequestParam(name = "id") Long id) throws ControllerException, ServiceException {
        if (id > 0) {
            OfferDto offerDto = offerService.findByIdContractor(id);
            return ResponseEntity.ok(offerDto);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

    /**
     * Find offer by id.
     *
     * @param id the id offer
     * @return the response entity
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/details-bidder")
    public ResponseEntity<OffersTenderBidderDto> findByIdBidder(@RequestParam(name = "id") Long id) throws ControllerException, ServiceException {
        if (id > 0) {
            OffersTenderBidderDto offersTenderBidderDto = offerService.findByIdBidder(id);
            return ResponseEntity.ok(offersTenderBidderDto);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

    /**
     * Update status offer reject by contractor
     *
     * @param id offer
     * @return updated offer
     * @throws ServiceException
     * @throws ControllerException
     */
    @PutMapping("/reject-contractor")
    public ResponseEntity<OfferDto> updateRejectByContractor(@RequestParam(name = "id") Long id) throws ServiceException, ControllerException {
        if (id > 0) {
            OfferDto result = offerService.updateRejectByContractor(id);
            return ResponseEntity.ok(result);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

    /**
     * Update status offer approved by contractor
     *
     * @param id offer
     * @return updated offer
     * @throws ServiceException
     * @throws ControllerException
     */
    @PutMapping("/approved-contractor")
    public ResponseEntity<OfferDto> updateApprovedByContractor(@RequestParam(name = "id") Long id) throws ServiceException, ControllerException {
        if (id > 0) {
            OfferDto result = offerService.updateApprovedByContractor(id);
            return ResponseEntity.ok(result);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

    /**
     * Update status offer approved by bidder and closed for tender
     *
     * @param id offer
     * @return updated offer
     * @throws ServiceException
     * @throws ControllerException
     */
    @PutMapping("/approved-bidder")
    public ResponseEntity<OfferDto> updateApprovedByBidder(@RequestParam(name = "id") Long id) throws ServiceException, ControllerException {
        if (id > 0) {
            OfferDto result = offerService.updateApprovedByBidder(id);
            return ResponseEntity.ok(result);
    } else {
        log.error("Negative id exception");
        throw new ControllerException("Negative id exception");
         }
    }

    /**
     * Update status offer declined by bidder
     *
     * @param id offer
     * @return updated offer
     * @throws ServiceException
     * @throws ControllerException
     */
    @PutMapping("/declined-bidder")
    public ResponseEntity<OfferDto> updateDeclinedByBidder(@RequestParam(name = "id") Long id) throws ServiceException, ControllerException {
        if (id > 0) {
            OfferDto result = offerService.updateDeclinedByBidder(id);
            return ResponseEntity.ok(result);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }
}
