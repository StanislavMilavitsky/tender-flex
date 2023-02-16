package pl.exadel.milavitsky.tenderflex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TenderDto extends CompanyDto implements Serializable {

    @Size(min = 2, max = 20, message = "CPV code should be between 2 and 20 characters")
    @NotEmpty(message = "Phone number should not be empty")
    private String cpvCode;

    @Size(min = 2, max = 30, message = "CPV description should be between 2 and 30 characters")
    private String cpvDescription;

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

    private Long idUser;
}
