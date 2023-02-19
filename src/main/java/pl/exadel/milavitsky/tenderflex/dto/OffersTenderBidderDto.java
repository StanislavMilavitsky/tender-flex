package pl.exadel.milavitsky.tenderflex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OffersTenderBidderDto extends CompanyDto implements Serializable {

    private Long id;

    private String cpvDescription;

    private String cpvCode;

    private String currency;

    private String bidPrice;

    private String sentDate;

    private String status;

    private String awardDecision;

    private String rejectDecision;

    private String contract;
}
