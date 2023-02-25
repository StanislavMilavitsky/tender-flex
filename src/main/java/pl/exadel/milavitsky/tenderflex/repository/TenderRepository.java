package pl.exadel.milavitsky.tenderflex.repository;

import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.Tender;

import java.util.List;

/**
 * Work with tender in layer repository
 */
public interface TenderRepository extends BaseRepository <Tender> {

    /**
     * Get count of Tenders at database not deleted
     *
     * @param idUser
     * @return count
     */
    long countOfTendersContractor(Long idUser);


    List<CPVCode> findAllCPVCodes();

}