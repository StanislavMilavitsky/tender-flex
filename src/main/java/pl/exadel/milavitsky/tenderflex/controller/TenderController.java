package pl.exadel.milavitsky.tenderflex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.exadel.milavitsky.tenderflex.dto.AddTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.CreateTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.FileDto;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.exception.ControllerException;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;
import pl.exadel.milavitsky.tenderflex.service.TenderService;
import pl.exadel.milavitsky.tenderflex.validation.sort.SortType;
import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

/**
 * Tender Rest Controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tender")
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
    public ResponseEntity<TenderDTO> findById(@PathVariable(name = "id") Long id) throws ControllerException, ServiceException {
        if (id > 0) {
            TenderDTO tenderDTO = tenderService.findById(id);
            return ResponseEntity.ok(tenderDTO);
        } else {
            log.error("Negative id exception");
            throw new ControllerException("Negative id exception");
        }
    }

    @GetMapping("/new")
    public ResponseEntity<AddTenderDTO> add()
            throws ServiceException {
            AddTenderDTO result = tenderService.collectTenderConstant();
            return  ResponseEntity.ok(result);
        }






    /**
     * Add tender.
     *
     * @param createTenderDTO the tender dto
     * @return the response entity
     * @throws ServiceException    the service exception
     * @throws ControllerException if entity fields not valid
     */

    @PostMapping()
    public ResponseEntity<CreateTenderDTO> create(@RequestBody @Valid CreateTenderDTO createTenderDTO, BindingResult bindingResult)
            throws ServiceException, ControllerException {
        if (bindingResult.hasErrors()) {
            log.error(bindingResultHandler(bindingResult));
            throw new ControllerException(bindingResultHandler(bindingResult));
        } else {
            CreateTenderDTO result = tenderService.create(createTenderDTO);
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
  /*  @PutMapping()
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
    }*/

    /**
     * Delete tender by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     * @throws ControllerException if id is incorrect
     */
    @DeleteMapping("/{id}")
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
     *
     * @param page the page
     * @param size count on page
     * @return tenders
     * @throws ServiceException the service exception
     * @throws IncorrectArgumentException incorrect argument
     */
    @Override
    @GetMapping("all-tenders")
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

    /**
     * Find all tenders by contractor All tenders created
     *
     * @param page the page
     * @param size count on page
     * @return list of tenders contractor
     * @throws ServiceException the service exception
     * @throws IncorrectArgumentException incorrect argument
     */
    @GetMapping("/{company}/all-tenders")
    public ResponseEntity<PagedModel<TenderDTO>> findAllByContractor(@PathVariable(name = "company") String  contractorCompany,
    @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        List<TenderDTO> tenders = tenderService.findAllByContractor(page, size, contractorCompany);
        long count = tenderService.count();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, count);
        List<Link> linkList = buildLink(page, size, (int) pageMetadata.getTotalPages());
        PagedModel<TenderDTO> pagedModel = PagedModel.of(tenders, pageMetadata, linkList);
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

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> upload(@ModelAttribute FileDto request) {
        return ResponseEntity.ok().body(tenderService.uploadFile(request));
    }

    @GetMapping(value = "/**")
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(tenderService.getObject(filename)));
    }

}
