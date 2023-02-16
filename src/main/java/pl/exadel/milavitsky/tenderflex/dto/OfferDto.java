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
public class OfferDto extends CompanyDto implements Serializable {

    private Long bidPrice;

    private String currency;

    private String document;

    private String status;

    private String sendDate;

    private Long idUser;

}
