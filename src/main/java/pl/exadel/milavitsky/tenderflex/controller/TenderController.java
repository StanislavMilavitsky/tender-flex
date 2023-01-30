package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.exception.ControllerException;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.service.TenderService;
import pl.exadel.milavitsky.tenderflex.validation.sort.SortType;

import javax.validation.Valid;
import java.util.List;

/**
 * Tender Rest Controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tenders")
public class TenderController extends PageController<TenderDTO> {

    private final TenderService tenderService;

    /**
     * Find tender by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException  the service exception
     * @throws ControllerException if id is incorrect
     */
    @GetMapping("/{id}/user")
    @PreAuthorize("hasAuthority('CONTRACTOR')")
    public ResponseEntity<TenderDTO> findById(@PathVariable(name = "id") Long id) throws ControllerException, ServiceException {
        if (id > 0) {
            TenderDTO tenderDTO = tenderService.findById(id);
            return ResponseEntity.ok(tenderDTO);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

    /**
     * Add tender.
     *
     * @param tenderDTO the tender dto
     * @return the response entity
     * @throws ServiceException    the service exception
     * @throws ControllerException if entity fields not valid
     */

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TenderDTO> create(@RequestBody @Valid TenderDTO tenderDTO, BindingResult bindingResult)
            throws ServiceException, ControllerException {
        if (bindingResult.hasErrors()) {
            log.error(bindingResultHandler(bindingResult));
            throw new ControllerException(bindingResultHandler(bindingResult));
        } else {
            TenderDTO result = tenderService.create(tenderDTO);
            return ResponseEntity.ok(result);
        }
    }

    /**
     * Update tender. Mark the fields that are not specified for updating null.
     *
     * @param tenderDTO the tender dto
     * @return the response entity
     * @throws ServiceException the service exception
     * @throws ControllerException if entity fields not valid
     */
    @PutMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TenderDTO> update(@RequestBody @Valid TenderDTO tenderDTO,
                                             BindingResult bindingResult) throws ServiceException, ControllerException {
        if (bindingResult.hasErrors()) {
            log.error(bindingResultHandler(bindingResult));
            throw new ControllerException(bindingResultHandler(bindingResult));
        } else {
            TenderDTO result = tenderService.update(tenderDTO);
            return ResponseEntity.ok(result);
        }
    }

    /**
     * Delete tender by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     * @throws ControllerException if id is incorrect
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) throws ServiceException, ControllerException {
        if (id > 0) {
            tenderService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            throw new ControllerException("Negative id exception");
        }

    }

    /**
     * Find all tenders
     * @param page the page
     * @param size count on page
     * @return tenders
     * @throws ServiceException the service exception
     * @throws IncorrectArgumentException incorrect argument
     */
    @Override
    @GetMapping("all-tenders")//todo
    @PostAuthorize("hasAuthority('Bidder')")//todo one more this list for constractor: 1 with him tenders. 2 offers for tenders?. 3 select one? 4 description of tender. 5 what details?
    public ResponseEntity<PagedModel<TenderDTO>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        List<TenderDTO> tags = tenderService.findAll(page, size);
        long count = tenderService.count();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, count);
        List<Link> linkList = buildLink(page, size, (int) pageMetadata.getTotalPages());
        PagedModel<TenderDTO> pagedModel = PagedModel.of(tags, pageMetadata, linkList);
        return ResponseEntity.ok(pagedModel);
    }


    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")//todo method not need ?
    public ResponseEntity<PagedModel<TenderDTO>> findAllByUser(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        List<TenderDTO> tags = tenderService.findAllByUser(page, size);
        long count = tenderService.count();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, count);
        List<Link> linkList = buildLink(page, size, (int) pageMetadata.getTotalPages());
        PagedModel<TenderDTO> pagedModel = PagedModel.of(tags, pageMetadata, linkList);
        return ResponseEntity.ok(pagedModel);
    }
    /**
     * Search tender by title or description part.
     *
     * @param part the part
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/part")
    public ResponseEntity<List<TenderDTO>> searchByNameOrDesc(@RequestParam(name = "part") String part)
            throws ServiceException {
        List<TenderDTO> tenderDTO = tenderService.searchByTitleOrDescription(part);
        return ResponseEntity.ok(tenderDTO);
    }

    /**
     * Sort tender by title.
     *
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/title")
    public ResponseEntity<List<TenderDTO>> sortByName(@RequestParam(name = "sort") SortType sortType) throws ServiceException {
        List<TenderDTO> tenderDTO = tenderService.sortByTitle(sortType);
        return ResponseEntity.ok(tenderDTO);
    }

    /**
     * Sort tenders by date of start.
     *
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("date-start")
    public ResponseEntity<List<TenderDTO>> sortByDateStart(@RequestParam(name = "sort") SortType sortType) throws ServiceException {
        List<TenderDTO> tenderDTO = tenderService.sortByDateStart(sortType);
        return ResponseEntity.ok(tenderDTO);
    }

    /**
     * Sort tenders by date of end.
     *
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/date-end")
    public ResponseEntity<List<TenderDTO>> sortByDateEnd(@RequestParam(name = "sort") SortType sortType) throws ServiceException {
        List<TenderDTO> tenderDTO = tenderService.sortByDateEnd(sortType);
        return ResponseEntity.ok(tenderDTO);
    }
}
