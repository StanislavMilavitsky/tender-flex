package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
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
import java.util.List;

/**
 * Offer Rest Controller
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offer")
public class OfferController extends PageController<OfferDto> {

    private final OfferService offerService;

    /**
     * Find tender by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/offers-contractor")
    public ResponseEntity<PagedModel<OfferDto>> findAll(@RequestParam(name = "id") Long id, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                       @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        List<OfferDto> offers = offerService.findOfferByIdTender(page, size, id);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, 1);
        List<Link> linkList = buildLink(page, size, (int) pageMetadata.getTotalPages());
        PagedModel<OfferDto> pagedModel = PagedModel.of(offers, pageMetadata, linkList);
        return ResponseEntity.ok(pagedModel);
    }

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

    @GetMapping("/menu-create")
    public ResponseEntity<AddOfferDTO> add()
            throws ServiceException {
        AddOfferDTO result = offerService.collectOfferConstant();
        return  ResponseEntity.ok(result);
    }



    @GetMapping("/offers-bidder")
    public ResponseEntity<PagedModel<OffersTenderBidderDto>> findAllByBidder(@RequestParam(value = "id") Long  idUser,
                                                                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                             @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        List<OffersTenderBidderDto> offers = offerService.findAllByBidder(page, size, idUser);
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, 1);
        List<Link> linkList = buildLink(page, size, (int) pageMetadata.getTotalPages());
        PagedModel<OffersTenderBidderDto> pagedModel = PagedModel.of(offers, pageMetadata, linkList);
        return ResponseEntity.ok(pagedModel);
    }


    /**
     * Find offer by id.
     *
     * @param id the id offer
     * @return the response entity
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/contractor")
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
    @GetMapping("/bidder")
    public ResponseEntity<OffersTenderBidderDto> findByIdBidder(@RequestParam(name = "id") Long id) throws ControllerException, ServiceException {
        if (id > 0) {
            OffersTenderBidderDto offersTenderBidderDto = offerService.findByIdBidder(id);
            return ResponseEntity.ok(offersTenderBidderDto);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

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

    @Override
    public ResponseEntity<PagedModel<OfferDto>> findAll(int page, int size) throws ServiceException, IncorrectArgumentException {
        return null;
    }
}
