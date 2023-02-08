package pl.exadel.milavitsky.tenderflex.mapper;

import org.springframework.stereotype.Component;
import pl.exadel.milavitsky.tenderflex.dto.TenderDTO;
import pl.exadel.milavitsky.tenderflex.entity.Tender;

import java.time.LocalDate;

@Component
public class TenderReadMapper implements Mapper<TenderDTO, Tender> {
    @Override
    public TenderDTO toDTO(Tender tender) {
        return new TenderDTO(
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
    public Tender fromDTO(TenderDTO tenderDTO) {
        return new Tender(
                tenderDTO.getId(),
                tenderDTO.getTitle(),
                tenderDTO.getTenderDescription(),
                tenderDTO.getBudget(),
                LocalDate.parse(tenderDTO.getDateOfStart()),
                LocalDate.parse(tenderDTO.getDateOfEnd()),
                tenderDTO.getUserCompany(),
                tenderDTO.getIsDeleted()
        );
    }
}