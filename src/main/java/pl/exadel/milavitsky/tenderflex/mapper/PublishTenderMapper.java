package pl.exadel.milavitsky.tenderflex.mapper;

import org.springframework.stereotype.Component;
import pl.exadel.milavitsky.tenderflex.dto.CreateTenderDTO;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.entity.Tender;

import java.time.LocalDate;

@Component
public class PublishTenderMapper implements Mapper<, Tender> {
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
        return new Tender(
                createTenderDTO.g
        );
    }
}