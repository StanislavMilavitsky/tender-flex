package pl.exadel.milavitsky.tenderflex.mapper;

import org.springframework.stereotype.Component;
import pl.exadel.milavitsky.tenderflex.dto.CreateTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.Tender;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;

import java.time.LocalDate;


@Component
public class PublishTenderMapper implements Mapper<CreateTenderDTO, Tender> {
    @Override
    public CreateTenderDTO toDTO(Tender tender) {
        return  new TenderDTO(
                tender.getId(),
                tender.getTitle(),
                tender.getTenderDescription(),
                tender.getBudget(),
                tender.getDateOfStart().toString(),
                tender.getDateOfEnd().toString(),
                tender.getContractorCompany(),
                tender.getIsDeleted()
        );
    }

    @Override
    public Tender fromDTO(CreateTenderDTO createTenderDTO) {
        Tender tender = new Tender();
        tender.setCpvCode(new CPVCode(createTenderDTO.getCpvCode(), null));
        tender.setTypeOfTender(TypeOfTender.valueOf(createTenderDTO.getTypeOfTender()));
        tender.setDescriptionOfTheProcurement(createTenderDTO.getDescriptionOfTheProcurement());
        tender.setMinimumTenderValue(createTenderDTO.getMinimumTenderValue());
        tender.setMaximumTenderValue(createTenderDTO.getMaximumTenderValue());
        tender.setCurrency(Currency.valueOf(createTenderDTO.getCurrency()));
        tender.setPublicationDate(createTenderDTO.getPublicationDate());
        tender.setDeadlineForOfferSubmission(createTenderDTO.getDeadlineForOfferSubmission());
        tender.setDeadlineForSigningContractSubmission(createTenderDTO.getDeadlineForSigningContractSubmission());
        tender

        return tender;
    }
}