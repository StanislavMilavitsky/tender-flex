package pl.exadel.milavitsky.tenderflex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Tender implements Serializable {

    @Positive(message = "Should be positive")
    private Long id;

    @Positive(message = "Should be positive")
    private Long idCompany;

    @Positive(message = "Should be positive")
    private Long idContactPerson;

    @Positive(message = "Should be positive")
    private Long cpvCode;

    @Size(min = 2, max = 30, message = "CPV description should be between 2 and 30 characters")
    private Long cpvDescription;

    private TypeOfTender typeOfTender;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 2, max = 20, message = "Title should be between 2 and 50 characters")
    private String officialName;

    @Size(min = 2, max = 250, message = "Description of the procurement should be between 2 and 250 characters")
    private String descriptionOfTheProcurement;

    @Positive(message = "Should be positive")
    private String minimumTenderValue;

    @Positive(message = "Should be positive")
    private String maximumTenderValue;

    @Size(min = 2, max = 100, message = "Tender description should be between 2 and 100 characters")
    private String tenderDescription;

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