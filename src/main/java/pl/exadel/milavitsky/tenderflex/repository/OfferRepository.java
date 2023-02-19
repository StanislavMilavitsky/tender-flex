package pl.exadel.milavitsky.tenderflex.repository;


import pl.exadel.milavitsky.tenderflex.dto.OffersTenderBidderDto;
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
     List<Offer> findAllOffersByIdTender(int offset, int limit, Long id) throws RepositoryException;

     Offer create(Offer offer) throws RepositoryException;

    /**
     * Count of offers
     *
     * @return count
     */
    long countOfEntity();

    List<OffersTenderBidderDto> findAllOffersByBidder(int offset, int limit, Long idUser) throws RepositoryException;

    Offer findByIdContractor(Long id) throws RepositoryException;

    OffersTenderBidderDto findByIdBidder(Long id) throws RepositoryException;

    Offer updateRejectByContractor(Offer offer) throws RepositoryException;

    Offer updateApprovedByContractor(Offer offer) throws RepositoryException;

    Offer updateApprovedByBidder(Offer offer) throws RepositoryException;

    Offer updateDeclinedByBidder(Offer offer) throws RepositoryException;
}
