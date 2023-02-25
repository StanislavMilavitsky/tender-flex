package pl.exadel.milavitsky.tenderflex.mapper;

import org.springframework.stereotype.Component;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusOffer;
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;

import java.time.LocalDate;


@Component
public class TenderMapper implements Mapper<TenderDto, Tender> {
    @Override
    public TenderDto toDTO(Tender tender) {

        TenderDto tenderDto = new TenderDto();
        if (tender.getTypeOfTender()!=null) tenderDto.setTypeOfTender(tender.getTypeOfTender().name());
        tenderDto.setDescriptionOfTheProcurement(tender.getDescriptionOfTheProcurement());
        tenderDto.setMinimumTenderValue(tender.getMinimumTenderValue());
        tenderDto.setMaximumTenderValue(tender.getMaximumTenderValue());
        if (tender.getCurrency()!=null) tenderDto.setCurrency(tender.getCurrency().name());
        if (tender.getPublicationDate()!=null) tenderDto.setPublicationDate(tender.getPublicationDate().toString());
        if (tender.getDeadlineForOfferSubmission()!=null) tenderDto.setDeadlineForOfferSubmission(tender.getDeadlineForOfferSubmission().toString());
        if (tender.getDeadlineForSigningContractSubmission()!=null)tenderDto.setDeadlineForSigningContractSubmission(tender.getDeadlineForSigningContractSubmission().toString());
        tenderDto.setContract(tender.getContract());
        tenderDto.setContract(tender.getAwardDecision());
        tenderDto.setRejectDecision(tender.getRejectDecision());
        tenderDto.setOfficialName(tender.getOfficialName());
        tenderDto.setNationalRegistrationNumber(tender.getNationalRegistrationNumber());
        if (tender.getCountry()!=null) tenderDto.setCountry(tender.getCountry().name());
        tenderDto.setCity(tender.getCity());
        tenderDto.setName(tender.getName());
        tenderDto.setSurname(tender.getSurname());
        tenderDto.setPhoneNumber(tender.getPhoneNumber());
        tenderDto.setCountOfOffers(tender.getCountOfOffers());
        tenderDto.setIdUser(tender.getIdUser());
        tenderDto.setCpvCode(tender.getCpvCode());
        tenderDto.setCpvDescription(tender.getCpvDescription());
        if (tender.getStatusOffer()!=null) tenderDto.setStatusOffer(tender.getStatusOffer().name());
        return tenderDto;
    }

    @Override
    public Tender fromDTO(TenderDto tenderDto) {
        Tender tender = new Tender();
        tender.setTypeOfTender(TypeOfTender.valueOf(tenderDto.getTypeOfTender()));
        tender.setDescriptionOfTheProcurement(tenderDto.getDescriptionOfTheProcurement());
        tender.setMinimumTenderValue(tenderDto.getMinimumTenderValue());
        tender.setMaximumTenderValue(tenderDto.getMaximumTenderValue());
        tender.setCurrency(Currency.valueOf(tenderDto.getCurrency()));
        tender.setPublicationDate(LocalDate.parse(tenderDto.getPublicationDate()));
        tender.setDeadlineForOfferSubmission(LocalDate.parse(tenderDto.getDeadlineForOfferSubmission()));
        tender.setDeadlineForSigningContractSubmission(LocalDate.parse(tenderDto.getDeadlineForSigningContractSubmission()));
        tender.setContract(tenderDto.getContract());
        tender.setAwardDecision(tenderDto.getAwardDecision());
        tender.setRejectDecision(tenderDto.getRejectDecision());
        tender.setOfficialName(tenderDto.getOfficialName());
        tender.setNationalRegistrationNumber(tenderDto.getNationalRegistrationNumber());
        tender.setCountry(Country.valueOf(tenderDto.getCountry()));
        tender.setCity(tenderDto.getCity());
        tender.setName(tenderDto.getName());
        tender.setSurname(tenderDto.getSurname());
        tender.setPhoneNumber(tenderDto.getPhoneNumber());
        tender.setIdUser(tenderDto.getIdUser());
        tender.setStatusOffer(StatusOffer.valueOf(tenderDto.getStatusOffer()));
        tender.setCpvCode(tenderDto.getCpvCode());
        tender.setCpvDescription(tenderDto.getCpvDescription());
        return tender;
    }
}