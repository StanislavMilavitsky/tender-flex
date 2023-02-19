package pl.exadel.milavitsky.tenderflex.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;
import pl.exadel.milavitsky.tenderflex.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

public abstract class PageController<T> {
    public abstract ResponseEntity<PagedModel<T>> findAll(int page, int size)
            throws ServiceException, IncorrectArgumentException;

    protected List<Link> buildLink(int page, int size, int maxPage)
            throws ServiceException, IncorrectArgumentException {
        List<Link> linkList = new ArrayList<>();
        Link self = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(this.getClass())
                        .findAll(page, size)
        ).withRel("self");
        linkList.add(self);
        if (page > 1) {
            Link previous = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder
                            .methodOn(this.getClass())
                            .findAll(page - 1, size)
            ).withRel("previous");
            linkList.add(previous);
        }
        if (maxPage > page) {
            Link next = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder
                            .methodOn(this.getClass())
                            .findAll(page + 1, size)
            ).withRel("next");
            linkList.add(next);
        }
        return linkList;
    }
    /**
     * Get default message by validate exception
     *
     * @param bindingResult exceptions of validate
     * @return string default message of exception
     */
    protected String bindingResultHandler(BindingResult bindingResult) {
        return bindingResult.getAllErrors().get(0).getDefaultMessage();
    }

}