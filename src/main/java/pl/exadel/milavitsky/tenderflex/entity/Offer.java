package pl.exadel.milavitsky.tenderflex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Offer {

    @Positive(message = "Should be positive")
    Long id;

    @Positive(message = "Should be positive")
    Long idTender;

    @NotEmpty(message = "Name of company should not be empty")
    @Size(min = 2, max = 20, message = "Name of company should be between 2 and 30 characters")
    String companyBidder;

    @Positive(message = "Budget should be positive")
    Double offer;

    @Size(min = 2, max = 100, message = "Title should be between 2 and 100 characters")
    String offerDescription;

    Boolean answer;
}
