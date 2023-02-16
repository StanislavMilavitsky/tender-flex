package pl.exadel.milavitsky.tenderflex.mapper;

import org.springframework.stereotype.Component;
import pl.exadel.milavitsky.tenderflex.dto.TenderDto;
import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;

import java.time.LocalDate;


@Component
public class TenderMapper implements Mapper<TenderDto, Tender> {
    @Override
    public TenderDto toDTO(Tender tender) {
        TenderDto tenderDto = new TenderDto();
        tenderDto.setCpvCode(tender.getCpvCode().getCpvCode());
        tenderDto.setCpvDescription(tender.getCpvCode().getDescriptionCPVCode());
        tenderDto.setTypeOfTender(tender.getTypeOfTender().name());
        tenderDto.setDescriptionOfTheProcurement(tender.getDescriptionOfTheProcurement());
        tenderDto.setMinimumTenderValue(tender.getMinimumTenderValue());
        tenderDto.setMaximumTenderValue(tender.getMaximumTenderValue());
        tenderDto.setCurrency(tender.getCurrency().name());
        tenderDto.setPublicationDate(tender.getPublicationDate().toString());
        tenderDto.setDeadlineForOfferSubmission(tender.getDeadlineForOfferSubmission().toString());
        tenderDto.setDeadlineForSigningContractSubmission(tender.getDeadlineForSigningContractSubmission().toString());
        tenderDto.setContract(tender.getContract());
        tenderDto.setContract(tender.getAwardDecision());
        tenderDto.setRejectDecision(tender.getRejectDecision());
        tenderDto.setOfficialName(tender.getOfficialName());
        tenderDto.setNationalRegistrationNumber(tender.getNationalRegistrationNumber());
        tenderDto.setCountry(tender.getCountry().name());
        tenderDto.setCity(tender.getCity());
        tenderDto.setName(tender.getName());
        tenderDto.setSurname(tender.getSurname());
        tenderDto.setPhoneNumber(tender.getPhoneNumber());
        tenderDto.setCountOfOffers(tender.getCountOfOffers());
        tenderDto.setIdUser(tender.getIdUser());
        return tenderDto;
    }

    @Override
    public Tender fromDTO(TenderDto tenderDto) {
        Tender tender = new Tender();
        tender.setCpvCode(new CPVCode(tenderDto.getCpvCode(), null));
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
        return tender;
    }
}