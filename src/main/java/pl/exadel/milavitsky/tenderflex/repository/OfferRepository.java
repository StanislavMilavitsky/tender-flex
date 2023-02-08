package pl.exadel.milavitsky.tenderflex.repository;


import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;

import java.util.List;

/**
 * Work with offer in layer repository
 */
public interface OfferRepository {

    /**
     * Find all offers by id tender
     *
     * @param id tender
     * @return list of offers
     */
    public List<Offer> findAllOffersByIdTender(int offset, int limit, Long id) throws RepositoryException;

    public Offer create(Offer offer) throws RepositoryException;

    /**
     * Count of offers
     *
     * @return count
     */
    long countOfEntity();
}
