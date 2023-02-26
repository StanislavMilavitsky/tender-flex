package pl.exadel.milavitsky.tenderflex.repository;


import org.springframework.data.domain.Pageable;
import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.exception.RepositoryException;

import java.util.List;

/**
 * Work with offer in layer repository
 */
public interface OfferRepository {

    /**
     * Find all offers by id contractor
     *
     * @param pageable size and page of view
     * @param idUser id
     * @return list of offers
     */
     List<Offer> findAllOffersByIdContractor(Pageable pageable, Long idUser) throws RepositoryException;

    /**
     * Create offer
     *
     * @param offer entity
     * @return created offer
     * @throws RepositoryException db exceptions
     */
     Offer create(Offer offer) throws RepositoryException;

    /**
     * Find all offers by id bidder
     *
     * @param pageable size and page of view
     * @param idUser id
     * @return list of offers
     */
    List<OffersTenderBidderDto> findAllOffersByBidder(Pageable pageable, Long idUser) throws RepositoryException;

    /**
     * Find offer by id contractor
     *
     * @param idUser id
     * @return offer entity
     * @throws RepositoryException db exceptions
     */
    Offer findByIdContractor(Long idUser) throws RepositoryException;

    /**
     * Find offer by id bidder
     *
     * @param idUser id
     * @return offer entity
     * @throws RepositoryException db exceptions
     */
    OffersTenderBidderDto findByIdBidder(Long idUser) throws RepositoryException;

    /**
     * Set status offer reject by contractor
     *
     * @param id offer
     * @return updated entity
     * @throws RepositoryException
     */
    Offer updateRejectByContractor(Long id) throws RepositoryException;

    /**
     * Set status offer approved by contractor
     *
     * @param id offer
     * @return updated entity
     * @throws RepositoryException
     */
    Offer updateApprovedByContractor(Long id) throws RepositoryException;

    /**
     * Set status offer approved by bidder and status tender closed
     *
     * @param id offer
     * @return updated entity
     * @throws RepositoryException
     */
    Offer updateApprovedByBidder(Long id) throws RepositoryException;

    /**
     * Set status offer reject declined by bidder
     *
     * @param id offer
     * @return updated entity
     * @throws RepositoryException
     */
    Offer updateDeclinedByBidder(Long id) throws RepositoryException;

    /**
     * Count of offers by contractor
     *
     * @param idUser id
     * @return count
     */
    long countOfOffersByContractor(Long idUser);

    /**
     * Count of offers by bidder
     *
     * @param idUser id
     * @return count
     */
    long countOfOffersByBidder(Long idUser);
}
