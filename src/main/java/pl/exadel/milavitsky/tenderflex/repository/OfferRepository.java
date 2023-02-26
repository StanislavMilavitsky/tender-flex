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
     * Find all offers by id tender
     *
     * @param idUser tender
     * @return list of offers
     */
     List<Offer> findAllOffersByIdContractor(Pageable pageable, Long idUser) throws RepositoryException;

     Offer create(Offer offer) throws RepositoryException;

    List<OffersTenderBidderDto> findAllOffersByBidder(Pageable pageable, Long idUser) throws RepositoryException;

    Offer findByIdContractor(Long id) throws RepositoryException;

    OffersTenderBidderDto findByIdBidder(Long id) throws RepositoryException;

    Offer updateRejectByContractor(Long id) throws RepositoryException;

    Offer updateApprovedByContractor(Long id) throws RepositoryException;

    Offer updateApprovedByBidder(Long id) throws RepositoryException;

    Offer updateDeclinedByBidder(Long id) throws RepositoryException;

    long countOfOffersByContractor(Long idUser);

    long countOfOffersByBidder(Long idUser);
}
