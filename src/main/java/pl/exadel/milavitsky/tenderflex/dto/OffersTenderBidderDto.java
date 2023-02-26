package pl.exadel.milavitsky.tenderflex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OffersTenderBidderDto extends CompanyDto implements Serializable {

    @Positive(message = "Should be positive")
    private Long id;

    private String cpvDescription;

    private String cpvCode;

    private String currency;

    @Positive(message = "Should be positive")
    private String bidPrice;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String sentDate;

    private String status;

    private String awardDecision;

    private String rejectDecision;

    private String contract;
}
