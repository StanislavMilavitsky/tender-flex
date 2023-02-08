package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
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
@RequestMapping("/api/v1/offers")
public class OfferController extends PageController<Offer> {

    private final OfferService offerService;

    /**
     * Find tender by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagedModel<Offer>> findOffersByTender(@PathVariable(name = "id") Long id,  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        List<Offer> offers = offerService.findOfferByIdTender(page, size, id);
        long count = offerService.count();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, count);
        List<Link> linkList = buildLink(page, size, (int) pageMetadata.getTotalPages());
        PagedModel<Offer> pagedModel = PagedModel.of(offers, pageMetadata, linkList);
        return ResponseEntity.ok(pagedModel);
    }

    @PostMapping()
    public ResponseEntity<Offer> create(@RequestBody @Valid Offer offer, BindingResult bindingResult)
            throws ServiceException, ControllerException {
        if (bindingResult.hasErrors()) {
            log.error(bindingResultHandler(bindingResult));
            throw new ControllerException(bindingResultHandler(bindingResult));
        } else {
            Offer result = offerService.create(offer);
            return ResponseEntity.ok(result);
        }
    }

    @Override
    public ResponseEntity<PagedModel<Offer>> findAll(int page, int size) throws ServiceException, IncorrectArgumentException {
        return null;
    }
}
