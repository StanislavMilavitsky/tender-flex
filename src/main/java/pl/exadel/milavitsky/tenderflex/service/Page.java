package pl.exadel.milavitsky.tenderflex.service;

import pl.exadel.milavitsky.tenderflex.exception.IncorrectArgumentException;

/**
 * Page entity
 */
public class Page {

    private final int page;
    private final int size;
    private final long countEntity;

    public Page(int page, int size, long countEntity) throws IncorrectArgumentException {
        if (page < 0) {
            throw new IncorrectArgumentException("Page index must not be less than zero!");
        }
        this.page = page;
        if (size < 1) {
            throw new IncorrectArgumentException("Page size must not be less than one!");
        }
        if ((page - 1) * size >= countEntity) {
            throw new IncorrectArgumentException("Page with index=" + page + " not exist!");
        }
        this.size = size;
        this.countEntity = countEntity;
    }

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }

}