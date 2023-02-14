package pl.exadel.milavitsky.tenderflex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusTender;
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Tender extends Company implements Serializable {

    @Positive(message = "Should be positive")
    protected Long id;

    private CPVCode cpvCode;

    private TypeOfTender typeOfTender;

    @Size(min = 2, max = 250, message = "Description of the procurement should be between 2 and 250 characters")
    private String descriptionOfTheProcurement;

    @Positive(message = "Should be positive")
    private String minimumTenderValue;

    @Positive(message = "Should be positive")
    private String maximumTenderValue;

    private Currency currency;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate deadlineForOfferSubmission;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate deadlineForSigningContractSubmission;

    private String contract;

    private String awardDecision;

    private String rejectDecision;

    private StatusTender statusTender ;

}