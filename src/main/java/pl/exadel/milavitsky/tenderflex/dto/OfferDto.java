package pl.exadel.milavitsky.tenderflex.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OfferDto extends CompanyDto implements Serializable {

    @Positive(message = "Should be positive")
    private Long id;

    @Positive(message = "Should be positive")
    private Long bidPrice;

    private String currency;

    private String document;

    private String status;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String sentDate;

    @Positive(message = "Should be positive")
    private Long idUser;

    @Positive(message = "Should be positive")
    private Long idTender;

}
