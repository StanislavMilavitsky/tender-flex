package pl.exadel.milavitsky.tenderflex.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TenderDto extends CompanyDto implements Serializable {

    private String typeOfTender;

    @Size(min = 2, max = 250, message = "Description of the procurement should be between 2 and 250 characters")
    private String descriptionOfTheProcurement;

    @Positive(message = "Should be positive")
    private String minimumTenderValue;

    @Positive(message = "Should be positive")
    private String maximumTenderValue;

    private String currency;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String publicationDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String deadlineForOfferSubmission;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String deadlineForSigningContractSubmission;

    private String contract;

    private String awardDecision;

    private String rejectDecision;

    private Integer countOfOffers;

    @Positive(message = "Should be positive")
    private Long idUser;

    private String statusOffer;
}
