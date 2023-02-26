package pl.exadel.milavitsky.tenderflex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusOffer;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Offer extends Company implements Serializable {

    @Positive(message = "Should be positive")
    protected Long id;

    @Positive(message = "Should be positive")
    private Long idTender;

    @Positive(message = "Budget should be positive")
    private Long bidPrice;

    private Currency currency;

    private String document;

    private StatusOffer status;

    @Positive(message = "Should be positive")
    private Long idUser;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate sentDate;


}
